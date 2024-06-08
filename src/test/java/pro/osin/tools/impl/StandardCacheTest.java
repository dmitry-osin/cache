package pro.osin.tools.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class StandardCacheTest {

    @Spy
    @InjectMocks
    private StandardCache<String, String> sut;

    public StandardCacheTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
    }
}