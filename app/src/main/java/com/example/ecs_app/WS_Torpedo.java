package com.example.ecs_app;

import com.example.ecs_app.Entidades.Area;
import com.example.ecs_app.Entidades.Bodega;
import com.example.ecs_app.Entidades.Celda;
import com.example.ecs_app.Entidades.Consignatario;
import com.example.ecs_app.Entidades.Contenedor;
import com.example.ecs_app.Entidades.Grua;
import com.example.ecs_app.Entidades.Gruero;
import com.example.ecs_app.Entidades.Marca;
import com.example.ecs_app.Entidades.Minera;
import com.example.ecs_app.Entidades.Paquete;
import com.example.ecs_app.Entidades.PaqueteCarro;
import com.example.ecs_app.Entidades.PaqueteManual;
import com.example.ecs_app.Entidades.Puerto;

import java.util.ArrayList;

/**
 * Interface metodos Web Service
 */
public interface WS_Torpedo {

    /**
     * Metodo Validar Credencial
     *
     * @param strings rutUsuario, contraseña     *
     * @return String respuesta
     */
    String validarCredencial(String... strings);

    /**
     * Metodo Listar Areas
     *
     * @return ArrayList<Area>
     */
    ArrayList<Area> ecs_listarAreas();

    /**
     * Metodo Listar Mineras
     *
     * @return ArrayList<Minera>
     */
    ArrayList<Minera> ecs_listaMineras();

    /**
     * Metodo buscar paquete por codigo de barra
     *
     * @param strings rutCliente, codigo
     * @return Paquete
     */
    Paquete ecs_BuscarPaquetesCB(String... strings);

    /**
     * Metodo buscar paquete manual
     *
     * @param strings rutCliente, lote, paquete
     * @return Paquete
     */
    Paquete ecs_BuscarPaquete(String... strings);

    /**
     * Metodo Recepcionar paquete
     *
     * @param strings intIdRelacionPaquete, rutUsuario, fechaRecepcion, turno
     * @return String respuesta
     */
    String ecs_RecepcionPaquete(String... strings);

    /**
     * Metodo Ingreso guia despacho manual
     *
     * @param strings rutMinera, numeroGuia, patente
     * @return Integer codigo
     */
    Integer ingresoGuiaManual(String... strings);

    /**
     * Metodo Ingreso Paquete manual
     *
     * @param strings intIdRelacionCarro,codigoLote,codigoPaquete,decPeso,rutUsuario,fechaRecepcion,turno
     * @return String codigo
     */
    String ingresoPaqueteManual(String... strings);

    /**
     * Metodo Buscar paquetes por carro
     *
     * @param strings codigoCarro
     * @return ArrayList<PaqueteManual> listaPaquetes
     */
    ArrayList<PaqueteManual> ecs_BuscarPaquetesPorCarro(String... strings);

    /**
     * Metodo Eliminar paquetes de forma manual
     *
     * @param strings intRelacionPaquete, rutUsuario
     * @return String codigo
     */
    String ecs_EliminarPaqueteManual(String... strings);

    /**
     * Metodo Almacena Paquete
     *
     * @param strings area,celda,intIdRelacionPaquete,rutUsuario,fecha,turno
     * @return String Respuesta
     */
    String ecs_AlmacenaPaquete(String... strings);

    /**
     * Metodo Listar Celdas segun el area
     *
     * @param strings codigoArea
     * @return ArrayList<Celda> listaCeldas
     */
    ArrayList<Celda> ecs_ListarCeldas(String... strings);

    /**
     * Metodo Despachar Paquete
     *
     * @param strings id,codigoArea,codigoCelda,rutUsuario,fecha,turno
     * @return String Respuesta
     */
    String ecs_Despachar(String... strings);

    /**
     * Metodo Remanejar Paquete
     *
     * @param strings Id, codigoAreaOrigen, codigoCeldaOrigen, codigoAreaDestino, codCeldaDestino, rutUsuario, fecha, turno
     * @return String Respuesta
     */
    String ecs_Remanejar(String... strings);

    /**
     * Metodo Listar Mineras segun correlativo recalada
     *
     * @param strings correlativoRecalada
     * @return ArrayList<Minera> ListaMineras
     */
    ArrayList<Minera> ecs_ListarMinerasRecalada(String... strings);

    /**
     * Metodo Registro Transferencia de paquete
     *
     * @param strings intRelacionIdPaquete, corRecalada, rutCliente,codPuertoDestino,codMarca,
     *                rutUsuario, fechaEmbarque,turno, codigoNave, codigoBodegaNave,
     *                codigoGrua, descripcionGrua, rutOperador, vchOperador
     * @return String[] codigo, descripcion
     */
    String[] ecs_RegistroTransferencia(String... strings);

    /**
     * Metodo Listar Puertos por cliente
     *
     * @param strings rutCliente, corRecalada
     * @return ArrayList<Puerto> listaPuertos
     */
    ArrayList<Puerto> ecs_ListarPuertos(String... strings);

    /**
     * Metodo Listar Marcas por cliente
     *
     * @param strings rutCliente, corRecalada
     * @return ArrayList<Marcas> listaMarcas
     */
    ArrayList<Marca> ecs_ListarMarcas(String... strings);

    /**
     * Metodo Listar Gruas
     *
     * @return ArrayList<Grua> listaGruas
     */
    ArrayList<Grua> ecs_ListarGruas();

    /**
     * Metodo Listar Operadores de grua
     *
     * @param strings corRecalada
     * @return ArrayList<Gruero> listaOperadores
     */
    ArrayList<Gruero> ecs_ListarOperadores(String... strings);

    /**
     * Metodo Listar Paras
     *
     * @return ArrayList<String> listaParas
     */
    ArrayList<String> ecs_ListarParas();

    /**
     * Metodo Registrar Para
     *
     * @param strings corRecalada,codTipoIncidente,fechaInicio,fechaTermino,rutCliente,codMarca,
     *                codBodegaNave,rutUsuario
     * @return String[] codigo,descripcion
     */
    String ecs_RegistroParas(String... strings);

    /**
     * Metodo Obtener listado de paquetes segun relacion
     *
     * @param strings corRecalada,rutCliente,codPuertoDestino,codMarca,codLote
     * @return ArrayList<PaqueteCarro> listaPaquetes
     */
    ArrayList<PaqueteCarro> ecs_ObtenerRelacionPaquete(String... strings);

    ArrayList<Bodega> ecs_ListarGruasNave(String... strings);

    Contenedor cfs_BuscaContenedor(String... strings);

    Consignatario cfs_BuscaConsignatario(String... strings);

    Marca cfs_BuscaMarca(String... strings);

    String cfs_RegistraConsolidado(String... strings);


}
