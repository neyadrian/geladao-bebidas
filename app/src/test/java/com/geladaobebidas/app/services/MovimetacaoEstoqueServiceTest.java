package com.geladaobebidas.app.services;

import com.geladaobebidas.app.repositories.MovimentacaoEstoqueRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MovimetacaoEstoqueServiceTest {

    @Mock
    private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

    @Mock
    private MovimentacaoEstoqueService movimentacaoEstoqueService;
}
