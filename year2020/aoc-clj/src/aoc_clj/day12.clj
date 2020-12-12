(ns aoc-clj.day12
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(def test-input
  (->> "day12_test.txt"
       io/resource
       slurp
       string/split-lines
       (map (fn [s] [(first s) (read-string (subs s 1))]))))

(def input
  (->> "day12.txt"
       io/resource
       slurp
       string/split-lines
       (map (fn [s] [(first s) (read-string (subs s 1))]))))

(defn p1 [ops]
  (let [deg->dydx {0 [-1 0], 90 [0 1], 180 [1 0], 270 [0 -1]}]
    (->> [ops [0 0] 90]
         (iterate
           (fn [[[[op mag] & ops'] pos dir]]
             (prn :pos pos) ; FIXME stack overflow error without this line !?
             (case op
               \N [ops' (nth (iterate (partial map + [-1 0]) pos) mag) dir]
               \S [ops' (nth (iterate (partial map + [1 0]) pos) mag) dir]
               \E [ops' (nth (iterate (partial map + [0 1]) pos) mag) dir]
               \W [ops' (nth (iterate (partial map + [0 -1]) pos) mag) dir]
               \L [ops' pos (mod (+ dir (- 360 mag)) 360)]
               \R [ops' pos (mod (+ dir mag) 360)]
               \F [ops' (nth (iterate (partial map + (deg->dydx dir)) pos) mag) dir])))
         (drop-while (comp seq first))
         ((fn [[[_ [dy dx]]]] (+ (Math/abs dy) (Math/abs dx)))))))

(defn p2 [ops]
  (->> [ops [0 0] [-1 10]]
       (iterate
         (fn [[[[op mag] & ops'] ship [wy wx :as waypt]]]
           (prn ship) ; FIXME stack overflow error without this line !?
           (case op
             \N [ops' ship (nth (iterate (partial map + [-1 0]) waypt) mag)]
             \S [ops' ship (nth (iterate (partial map + [1 0]) waypt) mag)]
             \E [ops' ship (nth (iterate (partial map + [0 1]) waypt) mag)]
             \W [ops' ship (nth (iterate (partial map + [0 -1]) waypt) mag)]
             \L [ops' ship (nth (iterate (fn [[y x]] [(- x) y]) waypt) (quot mag 90))]
             \R [ops' ship (nth (iterate (fn [[y x]] [x (- y)]) waypt) (quot mag 90))]
             \F [ops' (nth (iterate (partial map + waypt) ship) mag) waypt])))
       (drop-while (comp seq first))
       ((fn [[[_ [dy dx]]]] (+ (Math/abs dy) (Math/abs dx))))))

(comment
  (= 25 (p1 test-input))
  (= 2297 (p1 input))
  (= 286 (p2 test-input))
  (= (p2 input)))
