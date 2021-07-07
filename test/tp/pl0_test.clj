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
    (is (= '[nil () [] :sin-errores [[0 3] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]] (inicializar-contexto-local '[nil () [] :sin-errores [[0] [[X VAR 0] [Y VAR 1] [INI PROCEDURE 1]]] 2 [[JMP ?]]])))
    ))

