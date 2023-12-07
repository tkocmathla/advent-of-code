(ns aoc-clj.day7
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]
   [taoensso.tufte :refer [p profile] :as tufte]))

(defonce test-input (->> "day7_test.txt" io/resource slurp))
(defonce input (->> "day7.txt" io/resource slurp))

(defn number [xs] (into {} (map vector xs (range))))
(def hand-cardinality (number [:five-of-a-kind :four-of-a-kind :full-house :three-of-a-kind :two-pair :one-pair :high-card]))
(def card-cardinality (number [\A \K \Q \J \T \9 \8 \7 \6 \5 \4 \3 \2]))

(defn upgrade-j [s]
  (let [s' (remove #{\J} s)]
    (if (empty? s')
      "AAAAA"
      (let [[card _] (last (sort-by (juxt val (comp card-cardinality key)) (frequencies s')))]
        (string/replace s #"J" (str card))))))

(defn to-hand [s upgrade-j?]
  (let [s (cond-> s upgrade-j? upgrade-j)
        freq (vals (frequencies s))
        pairs #(count (filter #{2} %))]
    (cond
      (= 1 (count (set s))) :five-of-a-kind
      (some #{4} freq) :four-of-a-kind
      (and (some #{3} freq) (= 1 (pairs freq))) :full-house
      (and (some #{3} freq) (some #{1} freq)) :three-of-a-kind
      (= 2 (pairs freq)) :two-pair
      (and (= 1 (pairs freq)) (= 3 (count (filter #{1} freq)))) :one-pair
      (= 5 (count freq)) :high-card)))

(defn parse-hand [upgrade-j? s]
  (let [[hand bid] (string/split s #" ")]
    [hand (to-hand hand upgrade-j?) (str-int bid)]))

(defn sort-hands [upgrade-j? [s hand _]]
  (let [card-cardinality' (cond-> card-cardinality upgrade-j? (assoc \J Integer/MAX_VALUE))]
    (vec (cons (hand-cardinality hand) (mapv card-cardinality' s)))))

(defn solve [s & [upgrade-j?]]
  (let [hands (map (partial parse-hand upgrade-j?) (string/split-lines s))]
    (->> (sort-by (partial sort-hands upgrade-j?) hands)
         reverse
         (reduce (fn [[i sum] [_ _ bid]] [(inc i) (+ sum (* i bid))]) [1 0])
         second)))

(defn p1 [s] (solve s))
(defn p2 [s] (solve s true))

(comment
  (= 6440 (p1 test-input))
  (= 247823654 (p1 input))

  (= 5905 (p2 test-input))
  (= 245461700 (p2 input)))
