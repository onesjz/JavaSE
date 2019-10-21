package com.oneso;

import com.oneso.Annotations.*;
import com.oneso.TestClass.RepositoryForTests;
import com.oneso.TestClass.ServiceForTests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ClassForTest {

    private RepositoryForTests repository;
    private ServiceForTests serviceForTests;

    @AfterAll
    public static void testAfterAll() {
        System.out.println("End tests AfterAll");
    }

    @BeforeAll
    public static void testBeforeAll() {
        System.out.println("Start tests BeforeAll");
    }

    @After
    public void testAfter() {
        System.out.println("End test After");
    }

    @Before
    public void testBefore() {
        System.out.println("Start test Before");
    }

    @Test
    @Description("test info")
    public void shouldReturnOne() {
        System.out.println("TEST");
    }

    @Test
    @Description("Test getHello method")
    public void shouldGetHello() {
        repository = new RepositoryForTests();
        serviceForTests = new ServiceForTests(repository);


        assertEquals(serviceForTests.getHello().toLowerCase(), "hello");
    }

    @Test
    @Description("Increment method is working")
    public void shouldWorkingMethod() {
        repository = spy(RepositoryForTests.class);
        serviceForTests = new ServiceForTests(repository);
        doReturn(1).when(repository).addOne(anyInt());

        assertEquals(serviceForTests.increment(100), 1);
        verify(repository, times(1)).addOne(anyInt());
    }

    @Test
    @Description("Test should be fail")
    public void shouldFailTest() {
        repository = new RepositoryForTests();
        serviceForTests = new ServiceForTests(repository);

        assertEquals(serviceForTests.getHello(), "qwe");
    }

    @Test
    @Description("Test should be return exception")
    public void shouldReturnException() {
        repository = new RepositoryForTests();
        serviceForTests = new ServiceForTests(repository);

        assertThrows(IllegalArgumentException.class, () -> serviceForTests.addNewData("qwe"));
    }
}
