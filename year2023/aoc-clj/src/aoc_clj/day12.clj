(ns aoc-clj.day12
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [taoensso.tufte :refer [p profile] :as tufte]
   [aoc-clj.util :refer [str-int]]))

(defonce test-input (->> "day12_test.txt" io/resource slurp))
(defonce input (->> "day12.txt" io/resource slurp))

(defn parse-line [s]
  (let [[springs sizes] (string/split s #" ")]
    [(seq springs) (map str-int (re-seq #"\d+" sizes))]))

(defn multiply-input [[springs sizes]]
  [(flatten (interpose \? (repeat 5 springs)))
   (flatten (repeat 5 sizes))])

(declare step)

(defn step- [[spring & springs] [size & sizes] gap? filling?]
  (cond
    (and (= \# spring) (= 1 size) (empty? sizes) gap?)
    (if (not-any? #{\#} springs) 1 0)

    (= \? spring)
    (+ (step (cons \# springs) (cons size sizes) gap? filling?)
       (step (cons \. springs) (cons size sizes) gap? filling?))

    (and (= \# spring) (= 1 size) gap?)
    (recur springs sizes false false)

    (and (= \# spring) (> size 1) gap?)
    (recur springs (cons (dec size) sizes) gap? true)

    (and (= \. spring) (not filling?))
    (recur springs (cons size sizes) true false)

    :else 0))

(def step (memoize step-))

(defn solve [f s]
  (let [inputs (map f (map parse-line (string/split-lines s)))]
     (reduce + (map (fn [[springs sizes]] (step springs sizes true false)) inputs))))

(defn p1 [s] (solve identity s))
(defn p2 [s] (solve multiply-input s))

(comment
  (tufte/add-basic-println-handler! {})

  (= 21 (p1 test-input))
  (profile {} (dotimes [_ 100] (p :p1 (= 7674 (p1 input)))))

  (= 525152 (p2 test-input))
  (profile {} (dotimes [_ 10] (p :p2 (= 4443895258186 (p2 input))))))
