(ns aoc-clj.day14
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(defn parse [file]
  (->> file
       io/resource
       slurp
       string/split-lines
       (map #(string/split % #" = "))))

(def test-input (parse "day14_test.txt"))
(def test-input2 (parse "day14_test2.txt"))
(def input (parse "day14.txt"))

; ------------------------------------------------------------------------------

(defn to-mask [mask]
  (keep-indexed (fn [i c] (case c \0 [i bit-clear] \1 [i bit-set] nil)) (reverse mask)))

(defn to-val [mask x]
  (reduce (fn [x' [i f]] (f x' i)) x mask))

(defn p1 [cmds]
  (->> [cmds 0 {}]
       (iterate
         (fn [[[[loc x] & cmds'] mask mem]]
           (if (= "mask" loc)
             [cmds' (to-mask x) mem]
             [cmds' mask (assoc mem (re-find #"\d+" loc) (to-val mask (read-string x)))])))
       (drop-while (comp seq first))
       ((fn [[[_ _ m]]] (reduce + (vals m))))))

; ------------------------------------------------------------------------------

(defn combos [cs]
  (if (empty? cs)
    '(())
    (for [xs (combos (rest cs)), x (first cs)]
      (cons x xs))))

(defn addrs [mask addr]
  (let [fix (keep-indexed (fn [i c] (when (= \1 c) i)) (reverse mask))
        flt (keep-indexed (fn [i c] (when (= \X c) i)) (reverse mask))]
    (->> (combos (repeat (count flt) [bit-clear bit-set]))
         (map (fn [combo] (reduce (fn [a [i f]] (f a i)) addr (map vector flt combo))))
         (map (fn [a] (reduce (fn [a' i] (bit-set a' i)) a fix))))))

(defn p2 [cmds]
  (->> [cmds nil {}]
       (iterate
         (fn [[[[loc x] & cmds'] mask mem]]
           (let [addr (re-find #"\d+" loc)]
             (if (= "mask" loc)
               [cmds' x mem]
               [cmds' mask (reduce (fn [m a] (assoc m a (read-string x))) mem (addrs mask (read-string addr)))]))))
       (drop-while (comp seq first))
       ((fn [[[_ _ m]]] (reduce + (vals m))))))

(comment
  (= 165 (p1 test-input))
  (= 14862056079561 (p1 input))
  (= 208 (p2 test-input2))
  (= 3296185383161 (p2 input)))
