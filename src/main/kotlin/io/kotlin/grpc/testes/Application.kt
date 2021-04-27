package io.kotlin.grpc.testes

import io.micronaut.runtime.Micronaut.*
fun main(args: Array<String>) {
	build()
	    .args(*args)
		.packages("io.kotlin.grpc.testes")
		.start()
}

