(ns aoc-clj.day11
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]))

(defonce test-input (->> "day11_test.txt" io/resource slurp))
(defonce input (->> "day11.txt" io/resource slurp))

(defn index-expansion [space]
  (let [indexed-space (zipmap (range) space)
        void? (partial every? #{\.})]
    (reduce (fn [set [i xs]] (cond-> set (void? xs) (conj i))) #{} indexed-space)))

(defn expanded-spaces [space]
  {:rows (index-expansion space), :cols (index-expansion (apply map vector space))})

(defn galaxies [space]
  (for [row (range (count space)), col (range (count (first space)))
        :when (= \# (get-in space [row col]))]
    [row col]))

(defn galaxy-pairs [gxs]
  (into #{} (for [g1 gxs, g2 gxs :when (not= g1 g2)] (sort [g1 g2]))))

(defn expanded-manhattan [n expansion [[y1 x1] [y2 x2]]]
  (let [within? (fn [a b x] (and (>= x (min a b)) (<= x (max a b))))
        exp-rows (filter (partial within? y1 y2) (:rows expansion))
        exp-cols (filter (partial within? x1 x2) (:cols expansion))
        dy (Math/abs (- y1 y2))
        dx (Math/abs (- x1 x2))]
    (+ (- dy (count exp-rows)) (- dx (count exp-cols)) (* (count exp-rows) n) (* (count exp-cols) n))))

(defn solve [n s]
  (let [space (string/split-lines s)
        expansion (expanded-spaces space)
        pairs (galaxy-pairs (galaxies space))]
    (reduce + (map (partial expanded-manhattan n expansion) pairs))))

(defn p1 [s] (solve 2 s))
(defn p2 [s] (solve 1000000 s))

(comment
  (= 9 (expanded-manhattan 2 {:rows #{3 7} :cols #{2 5 8}} [[5 1] [9 4]]))
  (= 15 (expanded-manhattan 2 {:rows #{3 7} :cols #{2 5 8}} [[0 3] [8 7]]))
  (= 17 (expanded-manhattan 2 {:rows #{3 7} :cols #{2 5 8}} [[2 0] [6 9]]))
  (= 5 (expanded-manhattan 2 {:rows #{3 7} :cols #{2 5 8}} [[9 0] [9 4]]))

  (= 1030 (solve 10 test-input))
  (= 8410 (solve 100 test-input))

  (= 374 (p1 test-input))
  (= 9543156 (p1 input))

  (= 625243292686 (p2 input)))
