(ns aoc-clj.day8
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def input
  (->> "day8.txt"
       io/resource
       slurp
       string/split-lines
       (mapv #(string/split % #" "))))

(defn p1 [inst]
  (let [exe (fn [[acc i seen]]
              (if (seen i)
                acc
                (let [[op n] (inst i)]
                  (case op
                    "nop" [acc (inc i) (conj seen i)]
                    "acc" [(+ acc (read-string n)) (inc i) (conj seen i)]
                    "jmp" [acc (+ i (read-string n)) (conj seen i)]))))]
    (->> (iterate exe [0 0 #{}])
         (drop-while (complement number?))
         first)))

(comment
  (= 1087 (p1 input))
  (= (p2 input)))
