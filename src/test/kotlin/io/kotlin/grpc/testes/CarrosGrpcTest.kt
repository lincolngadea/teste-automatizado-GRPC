package io.kotlin.grpc.testes
import io.grpc.ManagedChannel
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.kotlin.grpc.testes.carros.Carro
import io.kotlin.grpc.testes.carros.CarroRepository
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import io.micronaut.grpc.server.GrpcServerChannel
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Singleton

/**
 * Importante desativar o controle transacional sempre que usar o GRPC
 * poi o Serv. Grpc não participa da transação que é aberta para cada teste
 * isso pode calsar erros ou gerar falsos positivos... isso vai fazer diferênça quando
 * trabalhamos como múltiplos testes
 */
@MicronautTest(transactional = false)
internal class CarrosGrpcTest(val repository: CarroRepository, val grpcClient: CarrosGrpcServiceGrpc.CarrosGrpcServiceBlockingStub) {

    /**
     * 1. Happy path
     * 2. quando já existe carro com a placa
     * 3. quando os dados de entrada são inválidos
     */



    @Test
    fun `deve adicionar um novo carro`() {

        //cenário

        //ação
        val response = grpcClient.adicionar(CarroRequest
            .newBuilder()
            .setModelo("Gol")
            .setPlaca("HPT-4444")
            .build())

        //validação
        with(response){
            assertNotNull(id)
            assertTrue(repository.existsById(id))
        }
    }

    @BeforeEach
    internal fun setUp() {
        repository.deleteAll()
    }

    @Test
    fun `não deve adicionar novo carro quando a placa já existir`(){
        //cenário
        val carroSalvo = repository.save(Carro("Hb20", "HDD-2004"))

        //ação
        val error = assertThrows<StatusRuntimeException>{
            grpcClient.adicionar(CarroRequest.newBuilder()
                .setModelo("Ferrari")
                .setPlaca(carroSalvo.placa)
                .build())
        }

        //validação
        with(error){
            assertEquals(Status.ALREADY_EXISTS.code, status.code)
            assertEquals("carro com placa existente", status.description)
        }

    }

    @Test
    fun `não deve adicionar novo carro quando dados de entrada forem invalidos`(){

        //cenário

        //ação
        val error = assertThrows<StatusRuntimeException>{
            grpcClient.adicionar(CarroRequest.newBuilder().build())
        }

        //validação
        with(error){
            assertEquals(Status.INVALID_ARGUMENT.code, status.code)
            assertEquals("dados de entrada inválidos", status.description)
        }
    }

    @Factory
    class Clients{
        /**
         * Quando criamos testes com GRPC, a porta de comunicação é aleatória
         * portanto, para podermos identificar em que porta o teste está rodando
         * usamos o @GrpcChannel(GrpcServerChannel.NAME)
         */
        @Singleton
        fun blockingStub(@GrpcChannel(GrpcServerChannel.NAME) channel: ManagedChannel): CarrosGrpcServiceGrpc.CarrosGrpcServiceBlockingStub?{
            return CarrosGrpcServiceGrpc.newBlockingStub(channel)
        }
    }

}
