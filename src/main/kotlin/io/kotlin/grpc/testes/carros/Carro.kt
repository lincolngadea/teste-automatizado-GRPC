package io.kotlin.grpc.testes.carros

import io.micronaut.core.annotation.Introspected
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
@Introspected
data class Carro(
    val modelo: String = "",
    val placa: String = ""
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var Id: Long? = null

}
