(ns aoc-clj.day9
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def test-input
  (->> "day9_test.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(def input
  (->> "day9.txt"
       io/resource
       slurp
       string/split-lines
       (mapv read-string)))

(defn p1 [preamble nums]
  (->> (drop preamble nums)
       (map-indexed
         (fn [i x]
           (let [ys (set (subvec nums i (+ i preamble)))]
             [x (some (fn [y] (contains? (disj ys y) (- x y))) ys)])))
       (drop-while (comp true? second))
       ffirst))

(defn p2 [preamble nums]
  (let [target (p1 preamble nums)]
    (->> (keep-indexed
           (fn [i x]
             (->> [[x] x (subvec nums (inc i))]
                  (iterate
                    (fn [[xs n [f & r]]]
                      (let [n' (+ n f)]
                        (cond
                          (= n' target) (conj xs f)
                          (< n' target) [(conj xs f) n' r]))))
                  (drop-while (comp vector? first))
                  first))
           (subvec nums 0 (dec (count nums))))
         first
         ((fn [xs] (+ (apply min xs) (apply max xs)))))))

(comment
  (= 127 (p1 5 test-input))
  (= 776203571 (p1 25 input))
  (= 62 (p2 5 test-input))
  (= 104800569 (p2 5 input)))
