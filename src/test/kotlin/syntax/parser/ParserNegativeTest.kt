package syntax.parser

import common.Diagnostic
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.StringContains.containsString
import org.junit.Assert.assertThrows
import org.junit.Test
import syntax.lexer.Lexer

class ParserNegativeTest {
    private fun assertDiagnostic(messageSubstring: String, sourceCode: String) {
        val lexer = Lexer(sourceCode)
        val parser = Parser(lexer)
        val diagnostic = assertThrows(Diagnostic::class.java) {
            parser.program()
        }
        assertThat(diagnostic.message, containsString(messageSubstring))
    }

    @Test
    fun tooManyClosingBraces() {
        assertDiagnostic("Too many closing braces", """
        void main() {
          }
        }
        """)
    }

    @Test
    fun commandMissingVoid() {
        assertDiagnostic("illegal start of command", """
        main() {
        }
        """)
    }

    @Test
    fun commandMissingName() {
        assertDiagnostic("missing IDENTIFIER", """
        void () {
        }
        """)
    }

    @Test
    fun commandMissingParameters() {
        assertDiagnostic("missing (", """
        void main {
        }
        """)
    }

    @Test
    fun commandMissingBody() {
        assertDiagnostic("missing {", """
        void main()
        """)
    }

    @Test
    fun nestedCommands() {
        assertDiagnostic("nested", """
        void outer() {
            void inner() {
            }
        }
        """)
    }

    @Test
    fun unclosedBlock() {
        assertDiagnostic("unclosed block", """
        void main() {
            if (onBeeper()) {
                pickBeeper();
        }
        """)
    }

    @Test
    fun numbersAreNotStatements() {
        assertDiagnostic("illegal start of statement", """
        void main() {
            123
        }
        """)
    }

    @Test
    fun commandMissingArguments() {
        assertDiagnostic("missing (", """
        void main() {
            other;
        }
        """)
    }

    @Test
    fun commandMissingSemicolon() {
        assertDiagnostic("missing ;", """
        void main() {
            other()
        }
        """)
    }

    @Test
    fun repeatMissingParens() {
        assertDiagnostic("missing (", """
        void main() {
            repeat 9 {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun repeatMissingNumber() {
        assertDiagnostic("missing NUMBER", """
        void main() {
            repeat () {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun repeatMissingBlock() {
        assertDiagnostic("missing {", """
        void main() {
            repeat (9)
                moveForward();
        }
        """)
    }

    @Test
    fun zeroRepetitions() {
        assertDiagnostic("0 out of range", """
        void main() {
            repeat (0) {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun oneRepetition() {
        assertDiagnostic("1 out of range", """
        void main() {
            repeat (1) {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun tooManyRepetitions() {
        assertDiagnostic("4096 out of range", """
        void main() {
            repeat (4096) {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun integerOverflow() {
        assertDiagnostic("2147483648 out of range", """
        void main() {
            repeat (2147483648) {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun ifMissingParens() {
        assertDiagnostic("missing (", """
        void main() {
            if frontIsClear() {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun ifMissingBlock() {
        assertDiagnostic("missing {", """
        void main() {
            if (frontIsClear())
                moveForward();
        }
        """)
    }

    @Test
    fun elseRequiresBlockOrIf() {
        assertDiagnostic("{ or if", """
        void main() {
            if (onBeeper()) {
                pickBeeper();
            }
            else dropBeeper();
        }
        """)
    }

    @Test
    fun whileMissingParens() {
        assertDiagnostic("missing (", """
        void main() {
            while frontIsClear() {
                moveForward();
            }
        }
        """)
    }

    @Test
    fun whileMissingBlock() {
        assertDiagnostic("missing {", """
        void main() {
            while (frontIsClear())
                moveForward();
        }
        """)
    }

    @Test
    fun statementAsCondition() {
        assertDiagnostic("illegal start of condition", """
        void main() {
            if (turnAround()) {
            }
        }
        """)
    }

    @Test
    fun conditionMissingParens() {
        assertDiagnostic("missing (", """
        void main() {
            while (frontIsClear) {
                moveForward();
            }
        }
        """)
    }
}
