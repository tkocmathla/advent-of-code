(ns aoc-clj.day2
 (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(def input
  (->> "day2.txt"
       io/resource
       slurp
       string/split-lines))

(defn p1 [lines]
  (let [f (fn [s]
            (let [[lo hi [k] pwd] (keep not-empty (string/split s #"[ :-]"))
                  freq (count (filter #{k} pwd))]
              (<= (read-string lo) freq (read-string hi))))]
    (count (filter f lines))))

(defn p2 [lines]
  (let [f (fn [s]
            (let [[lo hi [k] pwd] (keep not-empty (string/split s #"[ :-]"))
                  is-lo (= k (get pwd (dec (read-string lo))))
                  is-hi (= k (get pwd (dec (read-string hi))))]
              (and (or is-lo is-hi) (not (and is-lo is-hi)))))]
    (count (filter f lines))))

(comment
  (= 477 (p1 input))
  (= 686 (p2 input)))

