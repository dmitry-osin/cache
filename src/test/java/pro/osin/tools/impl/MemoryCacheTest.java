package pro.osin.tools.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class MemoryCacheTest {

    @Spy
    @InjectMocks
    private MemoryCache<String, String> sut;

    public MemoryCacheTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
    }
}