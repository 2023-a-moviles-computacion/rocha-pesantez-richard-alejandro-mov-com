package com.finance_tracker.finance_tracker.features.category_settings

import com.finance_tracker.finance_tracker.core.common.stateIn
import com.finance_tracker.finance_tracker.core.common.view_models.BaseViewModel
import com.finance_tracker.finance_tracker.core.ui.tab_rows.TransactionTypeTab
import com.finance_tracker.finance_tracker.core.ui.tab_rows.TransactionTypesMode
import com.finance_tracker.finance_tracker.core.ui.tab_rows.toTransactionType
import com.finance_tracker.finance_tracker.domain.interactors.CategoriesInteractor
import com.finance_tracker.finance_tracker.domain.models.Category
import com.finance_tracker.finance_tracker.features.category_settings.analytcis.CategorySettingsAnalytics
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategorySettingsViewModel(
    private val categoriesInteractor: CategoriesInteractor,
    private val categorySettingsAnalytics: CategorySettingsAnalytics
): BaseViewModel<CategorySettingsAction>() {

    private val expenseCategories = MutableStateFlow<ImmutableList<Category>>(persistentListOf())
    private val incomeCategories = MutableStateFlow<ImmutableList<Category>>(persistentListOf())

    private val transactionTypes = TransactionTypesMode.Main.types
    private val _selectedTransactionType = MutableStateFlow(TransactionTypeTab.Expense)
    val selectedTransactionType = _selectedTransactionType.asStateFlow()
    val transactionTypeTabPage = selectedTransactionType
        .map { transactionTypes.indexOf(it) }
        .stateIn(viewModelScope, initialValue = 0)
    val transactionTypesCount = transactionTypes.size

    init {
        categorySettingsAnalytics.trackScreenOpen()
    }

    fun onTransactionTypeSelect(transactionType: TransactionTypeTab) {
        categorySettingsAnalytics.trackTransactionTypeClick(transactionType)
        _selectedTransactionType.value = transactionType
    }

    fun onPageChanged(page: Int) {
        val transactionType = getTransactionTypeByPage(page)
        categorySettingsAnalytics.trackTransactionTypeClick(transactionType)
        _selectedTransactionType.value = transactionType
    }

    fun onScreenComposed() {
        loadAllCategories()
    }

    fun getCategories(page: Int): StateFlow<ImmutableList<Category>> {
        return when (getTransactionTypeByPage(page)) {
            TransactionTypeTab.Expense -> expenseCategories
            TransactionTypeTab.Income -> incomeCategories
            TransactionTypeTab.Transfer -> MutableStateFlow(persistentListOf())
        }
    }

    private fun getTransactionTypeByPage(page: Int): TransactionTypeTab {
        return transactionTypes.getOrNull(page) ?: error("No transaction type for page $page")
    }

    private fun loadAllCategories() {
        loadAllExpenseCategories()
        loadAllIncomeCategories()
    }

    private fun loadAllExpenseCategories() {
        viewModelScope.launch {
            expenseCategories.value = categoriesInteractor.getAllExpenseCategories()
                .toImmutableList()
        }
    }

    private fun loadAllIncomeCategories() {
        viewModelScope.launch {
            incomeCategories.value = categoriesInteractor.getAllIncomeCategories()
                .toImmutableList()
        }
    }

    fun swapExpenseCategories(from: Int, to: Int) {
        val fromItem = expenseCategories.value[from]
        val toItem = expenseCategories.value[to]
        val newList = expenseCategories.value.toMutableList()
        newList[from] = toItem
        newList[to] = fromItem

        categorySettingsAnalytics.trackSwapItems(
            from = from,
            to = to,
            category = fromItem
        )

        expenseCategories.value = newList.toImmutableList()
        saveCategoriesOrder(
            categoryId1 = fromItem.id, newPosition1 = to,
            categoryId2 = toItem.id, newPosition2 = from
        )
    }

    fun swapIncomeCategories(from: Int, to: Int) {
        val fromItem = incomeCategories.value[from]
        val toItem = incomeCategories.value[to]
        val newList = incomeCategories.value.toMutableList()
        newList[from] = toItem
        newList[to] = fromItem

        categorySettingsAnalytics.trackSwapItems(
            from = from,
            to = to,
            category = fromItem
        )

        incomeCategories.value = newList.toImmutableList()
        saveCategoriesOrder(
            categoryId1 = fromItem.id, newPosition1 = to,
            categoryId2 = toItem.id, newPosition2 = from
        )
    }

    private fun saveCategoriesOrder(
        categoryId1: Long, newPosition1: Int,
        categoryId2: Long, newPosition2: Int
    ) {
        viewModelScope.launch {
            categoriesInteractor.updateCategoryPosition(
                categoryId1, newPosition1,
                categoryId2, newPosition2
            )
        }
    }

    fun onConfirmDeleteCategory(category: Category, dialogKey: String) {
        categorySettingsAnalytics.trackConfirmDeleteCategoryClick(category)
        viewModelScope.launch {
            categoriesInteractor.deleteCategoryById(category.id)

            if (_selectedTransactionType.value == TransactionTypeTab.Expense) {
                loadAllExpenseCategories()
            } else {
                loadAllIncomeCategories()
            }
        }
        viewAction = CategorySettingsAction.DismissDialog(dialogKey)
    }

    fun onCancelDeleting(category: Category, dialogKey: String) {
        categorySettingsAnalytics.trackCancelDeleteCategoryClick(category)
        viewAction = CategorySettingsAction.DismissDialog(dialogKey)
    }

    fun onBackClick() {
        categorySettingsAnalytics.trackBackClick()
        viewAction = CategorySettingsAction.CloseScreen
    }

    fun onAddCategoryClick() {
        categorySettingsAnalytics.trackAddCategoryClick()
        viewAction = CategorySettingsAction.OpenAddCategoryScreen(
            selectedTransactionTypeTab = selectedTransactionType.value
        )
    }

    fun onDeleteCategoryClick(category: Category) {
        categorySettingsAnalytics.trackDeleteCategoryClick(category)
        viewAction = CategorySettingsAction.OpenDeleteDialog(category)
    }

    fun onCategoryCardClick(category: Category) {
        categorySettingsAnalytics.trackCategoryClick(category)
        viewAction = CategorySettingsAction.OpenEditCategoryScreen(
            category = category,
            transactionType = selectedTransactionType.value.toTransactionType()
        )
    }
}