package io.kotlin.grpc.testes.carros

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
class CarroRepository: JpaRepository<Carro, Long> {

}
