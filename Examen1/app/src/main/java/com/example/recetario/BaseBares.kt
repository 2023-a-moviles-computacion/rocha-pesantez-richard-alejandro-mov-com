package com.example.recetario

import java.text.SimpleDateFormat
import java.util.*

class BaseBares {
    companion object {
        val arregloBares = arrayListOf<Bar>()

        init {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd")
            val fechaIntegracion = dateFormat.parse("2022-05-15")

            val coctel1 = arrayListOf<Coctel>(
                Coctel(1, "Margarita", 200, 12.56, Date(), true, arrayOf("Tequila", "triple seco", "jugo de lima"), "Mezclar todo en un vaso coctelero"),
                Coctel(2, "Mojito", 200, 13.40, Date(), true, arrayOf("Ron", "azúcar", "menta","lima", "agua con gas o gaseosa"), "Mezclar todo en un vaso coctelero")
            )

            val coctel2 = arrayListOf<Coctel>(
                Coctel(3, "Gintonic", 325, 8.50, Date(), false, arrayOf("Ginebra ", "tónica"), "Mezclar todo en un vaso coctelero"),
                Coctel(4, "Caipirinha", 325, 7.25, Date(), true, arrayOf("Cachaza", "lima", "azúcar", "hielo"), "Mezclar todo en un vaso coctelero")
            )

            val coctel3 = arrayListOf<Coctel>(
                Coctel(5, "Manhattan", 150, 9.30, Date(), true, arrayOf("Whisky", "vermut rojo"), "Mezclar todo en un vaso coctelero"),
                Coctel(6, "Piña colada", 175, 8.90, Date(), false, arrayOf("Piña", "crema de coco","ron"), "Mezclar todo en un vaso coctelero")
            )

            arregloBares.add(
                Bar(1, "Abismo", 25, 45.50, fechaIntegracion, false, coctel1)
            )
            arregloBares.add(
                Bar(2, "Deja-vu", 70, 80.00, fechaIntegracion, true, coctel2)
            )
            arregloBares.add(
                Bar(3, "Sokalo", 40, 75.50, Date(), true, coctel3)
            )
        }
    }
}
