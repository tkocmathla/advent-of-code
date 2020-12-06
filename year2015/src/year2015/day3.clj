(ns year2015.day3
  (:require
    [clojure.java.io :as io]
    [clojure.set :as set]
    [clojure.string :as string]))

(defonce input
  (->> "day3.txt"
       io/resource
       slurp))

(defn coords [s]
  (let [f (fn [[[dy dx] [c & s]]]
            [[((case c \^ dec \v inc identity) dy)
              ((case c \< dec \> inc identity) dx)]
             s])]
    (->> (iterate f [[0 0] s])
         (take-while (comp seq second))
         (map first)
         set)))

(defn p1 [s]
  (count (coords (seq s))))

(defn p2 [s]
  (let [santa (coords (apply concat (partition 1 2 s)))
        rsanta (coords (apply concat (partition 1 2 (rest s))))]
    (count (set/union santa rsanta))))

(comment
  (= 2081 (p1 input))
  (= 2341 (p2 input)))
