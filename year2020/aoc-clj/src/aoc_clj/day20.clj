(ns aoc-clj.day20
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]
    [instaparse.core :as insta]))

(defn parse [file] (->> file io/resource slurp string/split-lines))
(def test-input (parse "day20_test.txt"))
(def input (parse "day20.txt"))

(defn parser [lines]
  (let [[a b c] (partition-by (partial re-find #"^0:") lines)]
    (->> (concat b a c)
         (string/join #"; ")
         (#(string/replace % #": " " = "))
         insta/parser)))

(defn p1 [lines]
  (let [[rules _ data] (partition-by empty? lines)
        p (parser rules)]
    (count (remove insta/failure? (map p data)))))

(defn fix [lines]
  (let [f (fn [s]
            (cond
              (string/starts-with? s "8:") "8: 42 | 42 8"
              (string/starts-with? s "11:") "11: 42 31 | 42 11 31"
              :else s))]
    (map f lines)))

(defn p2 [lines]
  (let [[rules _ data] (partition-by empty? lines)
        p (parser (fix rules))]
    (count (remove insta/failure? (map p data)))))

(comment
  (= 2 (p1 test-input))
  (= 205 (p1 input))
  (= 329 (p2 input)))
