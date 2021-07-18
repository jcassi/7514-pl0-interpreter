(ns tp.pl0-test
  (:require [clojure.test :refer :all]
            [tp.pl0 :refer :all]))

(deftest palabra-reservada?-test
  (testing "Prueba de la función: palabra-reservada?"
    (is (= true (palabra-reservada? 'CALL)))
    (is (= true (palabra-reservada? "CALL")))
    (is (= false (palabra-reservada? 'ASIGNAR)))
    (is (= false (palabra-reservada? "ASIGNAR")))))

(deftest cadena?-test
  (testing "Prueba de la función: palabra-reservada?"
    (is (true? (cadena? "'Hola'")))
    (is (false? (cadena? "Hola")))
    (is (false? (cadena? "'Hola")))
    (is (false? (cadena? 'Hola)))))

(deftest ya-declarado-localmente?-test
  (testing "Prueba"
    (is (true? (ya-declarado-localmente? 'Y '[[0] [[X VAR 0] [Y VAR 1]]])))
    (is (false? (ya-declarado-localmente? 'Z '[[0] [[X VAR 0] [Y VAR 1]]])))
    (is (false? (ya-declarado-localmente? 'Y '[[0 3 5] [[X VAR 0] [Y VAR 1] [INICIAR PROCEDURE 1] [Y CONST 2] [ASIGNAR PROCEDURE 2]]])))
    (is (true? (ya-declarado-localmente? 'Y '[[0 3 5] [[X VAR 0] [Y VAR 1] [INICIAR PROCEDURE 1] [Y CONST 2] [ASIGNAR PROCEDURE 2] [Y CONST 6]]])))))

(deftest cargar-var-en-tabla?-test                          ;No terminado
  (testing "Prueba de la función: cargar-var-en-tabla"
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]]) ))))

(deftest inicializar-contexto-local-test
  (testing "Prueba de"
    (is (= '[nil () [] :error [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]] (inicializar-contexto-local '[nil () [] :error [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]])))
    (is (= '[nil () [] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]] (inicializar-contexto-local '[nil () [] :sin-errores [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]])))))

(deftest aplicar-aritmetico-test                            ;No terminado
  (testing "Prueba de la función aplicar-aritmetico"
    (is (= [3] (aplicar-aritmetico + [1 2])))
    (is (= [1 3] (aplicar-aritmetico - [1 4 1])))
    (is (= [1 8] (aplicar-aritmetico * [1 2 4])))
    (is (= [1 0] (aplicar-aritmetico / [1 2 4])))
    (is (= nil (aplicar-aritmetico + nil)))
    (is (= [] (aplicar-aritmetico + [])))
    (is (= [1] (aplicar-aritmetico + [1])))
    ;(is (= [1 2 4] (aplicar-aritmetico 'hola [1 2 4])))
    ;(is (= [1 2 4] (aplicar-aritmetico count [1 2 4])))
    ;(is (= [a b c] (aplicar-aritmetico + '[a b c])))
    ))

(deftest aplicar-relacional-test                            ;No terminado
  (testing "Prueba de la función aplicar-relacional"
    (is (= [1] (aplicar-relacional > [7 5])))
    (is (= [4 1] (aplicar-relacional > [4 7 5])))
    (is (= [4 0] (aplicar-relacional = [4 7 5])))
    (is (= [4 1] (aplicar-relacional not= [4 7 5])))
    (is (= [4 0] (aplicar-relacional < [4 7 5])))
    (is (= [4 1] (aplicar-relacional <= [4 6 6])))
    ;(is (= [a b c] (aplicar-relacional <= '[a b c])))
    ))

(deftest dump-test
  (testing "Prueba de la función dump"
    (is (= (dump '[[PFM 0] [PFI 2] MUL [PFI 1] ADD NEG])))
    (is (= (dump '[HLT])))
    (is (= (dump nil)))))

(deftest generar-test
  (testing "Prueba de la función generar"
    (is (= '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?] HLT]] (generar '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]] 'HLT)))
    (is (= '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?] [PFM 0]]] (generar '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]] 'PFM 0)))
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (generar '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] 'HLT)))
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (generar '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] 'PFM 0)))))

(deftest buscar-coincidencias-test
  (testing "Prueba de la función buscar-coincidencias"
    (is (= '([X VAR 0] [X VAR 2]) (buscar-coincidencias '[nil () [CALL X] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [A PROCEDURE 1] [X VAR 2] [Y VAR 3] [B PROCEDURE 2]]] 6 [[JMP ?] [JMP 4] [CAL 1] RET]])))))

(deftest fixup-test
  (testing "Prueba de la función fixup"
    (is (= '[WRITELN (END .) [] :error [[0 3] []] 6  [[JMP ?] [JMP ?] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :error [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6  [[JMP ?] [JMP 4] [CAL 1] RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] 1)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6  [[JMP 8] [JMP 4] [CAL 1] RET [PFM 2] OUT NL RET]] (fixup ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP 4] [CAL 1] RET [PFM 2] OUT NL RET]] 0)))))

(deftest generar-signo-test
  (testing "Prueba de la función generar-signo"
    (is (= '[nil () [] :error [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :error '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '-)))
    (is (= '[nil () [] :error [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :error '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '+)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '+)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '*)))
    (is (= '[nil () [] :sin-errores [[0] [[X VAR 0]]] 1 [MUL ADD NEG]] (generar-signo [nil () [] :sin-errores '[[0] [[X VAR 0]]] 1 '[MUL ADD]] '-)))
    ))

(deftest generar-operador-relacional-test
  (testing "Prueba de la función generar-operador-relacional"
    (is (= '[WRITELN (END .) [] :error [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :error [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '+)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET EQ]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '=)))
    (is (= '[WRITELN (END .) [] :sin-errores [[0 3] []] 6 [[JMP ?] [JMP ?] [CAL 1] RET GTE]] (generar-operador-relacional ['WRITELN (list 'END (symbol ".")) [] :sin-errores [[0 3] []] 6 '[[JMP ?] [JMP ?] [CAL 1] RET]] '>=)))))

(deftest cargar-var-en-tabla?-test
  (testing "Prueba de la función cargar-var-en-tabla"
    (is (= '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :error [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X] :sin-errores [[0] []] 0 [[JMP ?]]])))
    (is (= '[nil () [VAR X Y] :sin-errores [[0] [[X VAR 0] [Y VAR 1]]] 2 [[JMP ?]]] (cargar-var-en-tabla '[nil () [VAR X , Y] :sin-errores [[0] [[X VAR 0]]] 1 [[JMP ?]]])))))

(deftest a-mayusculas-salvo-strings-test
  (testing "Prueba de la función a-mayusculas-salvo-strings"
    (is (= "  CONST Y = 2;" (a-mayusculas-salvo-strings "  const Y = 2;")))
    (is (= "  WRITELN ('Se ingresa un valor, se muestra su doble.');" (a-mayusculas-salvo-strings "  writeln ('Se ingresa un valor, se muestra su doble.');")))))

(deftest procesar-signo-unario-test
  (testing "Prueba de la función signo unario"
    (is (= ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :error '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))
    (is (= [7 (list (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario [7 (list (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))
    (is (= [7 (list (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=") '+] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario ['+ (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))
    (is (= ['7 (list (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=") '-] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []] (procesar-signo-unario ['- (list 7 (symbol ";") 'Y ':= '- 12 (symbol ";") 'END (symbol ".")) ['VAR 'X (symbol ",") 'Y (symbol ";") 'BEGIN 'X (symbol ":=")] :sin-errores '[[0] [[X VAR 0] [Y VAR 1]]] 2 []])))))
