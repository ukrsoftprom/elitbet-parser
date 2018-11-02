package com.elitbet.parser.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.powermock.api.mockito.PowerMockito.doThrow;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
public class FootballCreateParserFlashScoreTest {
    @Test
    public void parseGoals() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "1-0");
        assertEquals(1,result[0]);
        assertEquals(0,result[1]);
    }
    @Test
    public void parseGoalsWithWrongParams() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "m-0");
        assertEquals(0,result[0]);
        assertEquals(0,result[1]);
    }
    @Test
    public void parseGoalsWithDefis() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "-");
        assertEquals(0,result[0]);
        assertEquals(0,result[1]);
    }
    @Test
    public void parseGoalsEmptyString() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "");
        assertEquals(0,result[0]);
        assertEquals(0,result[1]);
    }
    @Test
    public void parseGoalsPenalty() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "2-2(4-5)");
        assertEquals(2,result[0]);
        assertEquals(2,result[1]);
    }
    @Test
    public void parseGoalsBraces() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        int[] result = (int[]) parseGoalsMethod.invoke(obj, "(0-2)");
        assertEquals(0,result[0]);
        assertEquals(0,result[1]);
    }
    @Test
    public void parseGoalsException() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Class<FootballCreateParserFlashScore> clazz = FootballCreateParserFlashScore.class;
        FootballCreateParserFlashScore obj = mock(FootballCreateParserFlashScore.class);
        Method parseGoalsMethod = clazz.getDeclaredMethod("parseGoals", String.class);
        parseGoalsMethod.setAccessible(true);
        String arg = mock(String.class);
        doThrow(new IndexOutOfBoundsException()).when(arg).split("-");
        int[] result = (int[]) parseGoalsMethod.invoke(obj, arg);
        assertEquals(0,result[0]);
        assertEquals(0,result[1]);
    }

}