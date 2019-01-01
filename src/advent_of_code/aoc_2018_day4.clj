(ns advent-of-code.aoc-2018-day4
  (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string])
  (:import [java.util Date]))

(def input
  (->> "2018-day4-input"
       io/resource
       slurp
       string/split-lines
       (map (fn [x]
              (let [[_ tm msg] (re-find #"\[(\d{4}-\d\d-\d\d \d\d:\d\d)\] (.+)" x)]
                {:tm (Date. (string/replace tm "-" "/"))
                 :msg msg})))))

(defn times []
  (->> [(sort-by :tm input) nil nil {}]
       (iterate
         (fn [[[x & xs] id t0 times]]
           (cond
             (not x) times

             (string/includes? (:msg x) "Guard")
             (let [[_ id] (re-find #"Guard #(\d+)" (:msg x))]
               [xs (read-string id) nil times])

             (string/includes? (:msg x) "falls asleep")
             (let [mins (.getMinutes (:tm x))]
               [xs id mins times])

             (string/includes? (:msg x) "wakes up")
             (let [new-times (update times id into (range t0 (.getMinutes (:tm x))))]
               [xs id nil new-times]))))
       (drop-while sequential?)
       first))

(defn part1 []
  (let [m (times)
        id (key (apply max-key (comp count val) m))
        mn (key (apply max-key val (frequencies (m id))))]
    (* id mn)))

(defn part2 []
  (let [m (times)]
    (->> (map (fn [[k v]] [k (apply max-key val (frequencies v))]) m)
         (apply max-key (comp second second))
         ((fn [[id [mn _]]] (* id mn))))))

(comment
  (= 50558 (part1))
  (= 28198 (part2)))
