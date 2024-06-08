package pro.osin.tools.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class DiskCacheTest {

    @Spy
    @InjectMocks
    private DiskCache<String, String> sut;

    public DiskCacheTest() {
        MockitoAnnotations.openMocks(this);
    }

    @BeforeEach
    void setUp() {
    }
}