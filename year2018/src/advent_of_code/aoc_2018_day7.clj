(ns advent-of-code.aoc-2018-day7
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.set :refer [difference]]
    [clojure.string :as string]))

(def input
  (->> "2018-day7-input"
       io/resource
       slurp
       string/split-lines
       (map #(map second (re-seq #"(?i)step (.)" %)))))

(defn part1 [input]
  (let [roots (difference (set (map first input)) (set (map second input)))
        g (reduce (fn [m [a b]] (update m b (fnil conj #{}) a)) {} input)]
    (->> [(apply assoc g (interleave roots (repeat #{}))) ""]
         (iterate
           (fn [[g s]]
             (let [x (ffirst (sort (filter (comp empty? val) g)))]
               [(reduce-kv (fn [m k v] (assoc m k (disj v x))) {} (dissoc g x))
                (str s x)])))
         (drop-while (comp not-empty first))
         first second)))

(comment
  (= "GJFMDHNBCIVTUWEQYALSPXZORK" (part1 input)))
