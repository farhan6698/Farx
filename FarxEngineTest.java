package com.farx.studio;

import org.junit.Test;
import static org.junit.Assert.*;

public class FarxEngineTest {
    @Test public void runsBasicProgram() {
        String code = "MODE CODE\nLET a = 8\nLET b = 2\nADD a b\nPRINT \"done\"\nEND MODE";
        FarxEngine.Result r = FarxEngine.run(code);
        assertTrue(r.output.contains("10"));
        assertTrue(r.output.contains("done"));
        assertTrue(r.summary.contains("SUCCESS"));
    }

    @Test public void languageIsCaseInsensitiveForKeywords() {
        FarxEngine.Result r = FarxEngine.run("mode code\nprint \"ok\"\nend mode");
        assertTrue(r.output.contains("ok"));
    }

    @Test public void detectsMissingEndMode() {
        try {
            FarxEngine.run("MODE CODE\nPRINT \"x\"");
            fail("Expected syntax error");
        } catch (FarxEngine.FarxException ex) {
            assertTrue(ex.getMessage().contains("Missing END MODE"));
        }
    }

    @Test public void detectsDivisionByZero() {
        try {
            FarxEngine.run("MODE CODE\nLET a = 5\nLET b = 0\nDIV a b\nEND MODE");
            fail("Expected division error");
        } catch (FarxEngine.FarxException ex) {
            assertTrue(ex.getMessage().contains("Division by zero"));
        }
    }

    @Test public void rejectsUnknownCommand() {
        try {
            FarxEngine.run("MODE CODE\nEXPLODE\nEND MODE");
            fail("Expected unknown command");
        } catch (FarxEngine.FarxException ex) {
            assertTrue(ex.getMessage().contains("Unknown command"));
        }
    }
}
