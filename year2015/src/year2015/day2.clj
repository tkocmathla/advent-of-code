(ns year2015.day2
  (:require
    [clojure.java.io :as io]
    [clojure.string :as string]))

(defonce input
  (->> "day2.txt"
       io/resource
       slurp
       string/split-lines))

(defn solve [f lines]
  (reduce + 0 (map (fn [s] (f (map read-string (string/split s #"x")))) lines)))

(def p1
  (partial solve (fn [[l w h]]
                   (let [a (* l w) b (* w h) c (* h l)]
                     (+ (min a b c) (* 2 (+ a b c)))))))

(def p2
  (partial solve (fn [[l w h :as v]]
                   (+ (* l w h) (* 2 (apply + (take 2 (sort v))))))))

(comment
  (= 1606483 (p1 input))
  (= 3842356 (p2 input)))
