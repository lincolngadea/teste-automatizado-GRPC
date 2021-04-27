package io.kotlin.grpc.testes.carros

import io.kotlin.grpc.testes.CarrosGrpcServiceGrpc
import javax.inject.Inject

class CarrosEndPoint(@Inject val repository: CarroRepository): CarrosGrpcServiceGrpc.CarrosGrpcServiceImplBase() {
}