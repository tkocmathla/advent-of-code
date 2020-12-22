(ns aoc-clj.day22
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(defn parse [file] (->> file io/resource slurp string/split-lines))
(defonce test-input (parse "day22_test.txt"))
(defonce input (parse "day22.txt"))

(defn decks [lines]
  (let [[_ s1 _ s2] (map vec (partition-by #(string/includes? % "Player") (remove #{""} lines)))]
    [(into (queue) (map read-string s1))
     (into (queue) (map read-string s2))]))

(defn queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll] (into (queue) coll)))

(defn score [[q1 q2]]
  (let [q (or (seq q1) (seq q2))]
    (reduce + (map * q (range (count q) 0 -1)))))

(defn p1 [lines]
  (->> (decks lines)
       (iterate
         (fn [[q1 q2]]
           (let [c1 (peek q1), c2 (peek q2)]
             (if (> c1 c2)
               [(conj (pop q1) c1 c2) (pop q2)]
               [(pop q1) (conj (pop q2) c2 c1)]))))
       (drop-while (partial every? seq))
       first
       score))

(comment
  (= 306 (p1 test-input))
  (= 34566 (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
