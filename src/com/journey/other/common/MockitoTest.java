package com.journey.other.common;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

/**
 * Mock 测试
 *
 * @author xiaxiangnan <xiaxiangnan@kuaishou.com>
 * Created on 2021-01-25
 */
@SuppressWarnings("all")
public class MockitoTest {

    @Test
    public void createMockObject() {
        // 使用 mock 静态方法创建 Mock 对象.
        List mockedList = mock(List.class);
        Assert.assertTrue(mockedList instanceof List);

        // mock 方法不仅可以 Mock 接口类, 还可以 Mock 具体的类型.
        ArrayList mockedArrayList = mock(ArrayList.class);
        Assert.assertTrue(mockedArrayList instanceof List);
        Assert.assertTrue(mockedArrayList instanceof ArrayList);
    }

    @Test
    public void configMockObject() {
        List mockedList = mock(List.class);
        //when(​...).thenReturn(​...) 方法链来定义一个行为
        // 我们定制了当调用 mockedList.add("one") 时, 返回 true
        when(mockedList.add("one")).thenReturn(true);
        // 当调用 mockedList.size() 时, 返回 1
        when(mockedList.size()).thenReturn(1);

        Assert.assertTrue(mockedList.add("one"));
        // 因为我们没有定制 add("two"), 因此返回默认值, 即 false.
        Assert.assertFalse(mockedList.add("two"));
        Assert.assertEquals(mockedList.size(), 1);

        Iterator i = mock(Iterator.class);
        when(i.next()).thenReturn("Hello").thenReturn("Mockito!");
        String result = i.next() + "," + i.next();
        Assert.assertEquals("Hello,Mockito!", result);
    }

    @Test(expected = NoSuchElementException.class)
    public void testForException() {
        Iterator i = mock(Iterator.class);
        //doThrow(ExceptionX).when(x).methodCall, 它的含义是: 当调用了 x.methodCall 方法后, 抛出异常 ExceptionX.
        //当调用 i.next() 后, 抛出异常 NoSuchElementException.
        doThrow(new NoSuchElementException()).when(i).next();
        i.next();
    }

    @Test
    public void testVerify() {
        List mockedList = mock(List.class);
        mockedList.add("one");
        mockedList.add("two");
        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");
        when(mockedList.size()).thenReturn(5);
        Assert.assertEquals(mockedList.size(), 5);

        //校验 mockedList.add("one") 至少被调用了 1 次
        verify(mockedList, atLeastOnce()).add("one");
        //mockedList.add("two") 被调用了 1 次
        verify(mockedList, times(1)).add("two");
        verify(mockedList, times(3)).add("three times");
        // mockedList.isEmpty() 从未被调用
        verify(mockedList, never()).isEmpty();
    }

    @Test
    public void testSpy() {
        List list = new LinkedList();
        //包装一个真实的 Java 对象
        List spy = spy(list);
        // 对 spy.size() 进行定制.
        when(spy.size()).thenReturn(100);
        spy.add("one");
        spy.add("two");
        // 因为我们没有对 get(0), get(1) 方法进行定制,因此这些调用其实是调用的真实对象的方法.
        Assert.assertEquals(spy.get(0), "one");
        Assert.assertEquals(spy.get(1), "two");
        Assert.assertEquals(spy.size(), 100);
    }

    @Test
    public void testCaptureArgument() {
        List<String> list = Arrays.asList("1", "2");
        List mockedList = mock(List.class);
        ArgumentCaptor<List> argument = ArgumentCaptor.forClass(List.class);
        mockedList.addAll(list);
        verify(mockedList).addAll(argument.capture());

        Assert.assertEquals(2, argument.getValue().size());
        Assert.assertEquals(list, argument.getValue());
    }


}
