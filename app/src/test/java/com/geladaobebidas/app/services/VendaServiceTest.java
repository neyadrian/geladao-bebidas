package com.geladaobebidas.app.services;

import com.geladaobebidas.app.dto.ItemVendaRequest;
import com.geladaobebidas.app.entities.Cliente;
import com.geladaobebidas.app.entities.Produto;
import com.geladaobebidas.app.entities.Usuario;
import com.geladaobebidas.app.entities.Venda;
import com.geladaobebidas.app.repositories.VendaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertThrows;
import com.geladaobebidas.app.exceptions.RecursoNaoEncontradoException;

@ExtendWith(MockitoExtension.class)
class VendaServiceTest {

    @Mock
    private VendaRepository vendaRepository;

    @Mock
    private ItemVendaService itemVendaService;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private MovimentacaoEstoqueService movimentacaoEstoqueService;

    @Mock
    private ClienteService clienteService;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private VendaService vendaService;

    @Test
    void deveRegistrarVendaComSucesso() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);
        clienteFake.setNomeCliente("Cliente Teste");

        Usuario usuarioFake = new Usuario();
        usuarioFake.setIdUsuario(1L);
        usuarioFake.setNomeUsuario("Usuario Teste");

        Produto produtoFake = new Produto();
        produtoFake.setIdProduto(1L);
        produtoFake.setNomeProduto("Cerveja Teste");
        produtoFake.setPrecoProduto(new BigDecimal("10.00"));
        produtoFake.setQuantidadeProduto(50);

        when(clienteService.buscarPorId(1L)).thenReturn(clienteFake);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioFake);
        when(produtoService.buscarPorId(1L)).thenReturn(produtoFake);

        ItemVendaRequest itemRequest = new ItemVendaRequest();
        itemRequest.setProdutoId(1L);
        itemRequest.setQuantidade(5);

        List<ItemVendaRequest> itens = new ArrayList<>();
        itens.add(itemRequest);

        Venda vendaResultado = vendaService.registrarVenda(1L, 1L, itens);

        assertEquals(new BigDecimal("50.00"), vendaResultado.getValorTotalVenda());
        assertEquals(clienteFake, vendaResultado.getCliente());
        assertEquals(usuarioFake, vendaResultado.getUsuario());
    }

    @Test
    void deveLancarExcecaoQuandoProdutoNaoExiste() {
        Cliente clienteFake = new Cliente();
        clienteFake.setIdCliente(1L);

        Usuario usuarioFake = new Usuario();
        usuarioFake.setIdUsuario(1L);

        when(clienteService.buscarPorId(1L)).thenReturn(clienteFake);
        when(usuarioService.buscarPorId(1L)).thenReturn(usuarioFake);
        when(produtoService.buscarPorId(99L))
                .thenThrow(new RecursoNaoEncontradoException("Produto não encontrado: 99"));

        ItemVendaRequest itemRequest = new ItemVendaRequest();
        itemRequest.setProdutoId(99L);
        itemRequest.setQuantidade(5);

        List<ItemVendaRequest> itens = new ArrayList<>();
        itens.add(itemRequest);

        assertThrows(RecursoNaoEncontradoException.class, () -> {
            vendaService.registrarVenda(1L, 1L, itens);
        });
    }
}