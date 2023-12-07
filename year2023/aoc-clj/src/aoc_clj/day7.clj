(ns aoc-clj.day7
  (:require
   [clojure.java.io :as io]
   [clojure.string :as string]
   [aoc-clj.util :refer [str-int]]
   [taoensso.tufte :refer [p profile] :as tufte]))

(defonce test-input (->> "day7_test.txt" io/resource slurp))
(defonce input (->> "day7.txt" io/resource slurp))

(defn number [xs] (into {} (map vector xs (range))))
(def hand-cardinality (number [:five-of-kind :four-of-kind :full-house :three-of-kind :two-pair :one-pair :high-card]))
(def card-cardinality (number [\A \K \Q \J \T \9 \8 \7 \6 \5 \4 \3 \2]))

(defn upgrade-j [cards]
  (let [cards' (remove #{\J} cards)
        best-card #(str (first (last (sort-by (juxt val (comp card-cardinality key)) (frequencies %)))))]
    (if (empty? cards')
      "AAAAA"
      (string/replace cards #"J" (best-card cards')))))

(defn to-hand [upgrade-j? s]
  (let [freq (vals (frequencies (cond-> s upgrade-j? upgrade-j)))
        pairs #(count (filter #{2} %))]
    (cond
      (= 1 (count freq)) :five-of-kind
      (some #{4} freq) :four-of-kind
      (and (some #{3} freq) (= 1 (pairs freq))) :full-house
      (and (some #{3} freq) (some #{1} freq)) :three-of-kind
      (= 2 (pairs freq)) :two-pair
      (and (= 1 (pairs freq)) (= 3 (count (filter #{1} freq)))) :one-pair
      (= 5 (count freq)) :high-card)))

(defn parse-hand [upgrade-j? s]
  (let [[hand bid] (string/split s #" ")]
    [hand (to-hand upgrade-j? hand) (str-int bid)]))

(defn sort-hands [upgrade-j? [s hand _]]
  (let [card-cardinality' (cond-> card-cardinality upgrade-j? (assoc \J Integer/MAX_VALUE))]
    (vec (cons (hand-cardinality hand) (mapv card-cardinality' s)))))

(defn solve [upgrade-j? s]
  (let [hands (map (partial parse-hand upgrade-j?) (string/split-lines s))]
    (->> (sort-by (partial sort-hands upgrade-j?) hands)
         reverse
         (reduce (fn [[i sum] [_ _ bid]] [(inc i) (+ sum (* i bid))]) [1 0])
         second)))

(defn p1 [s] (solve false s))
(defn p2 [s] (solve true s))

(= 6440 (p1 test-input))
(= 247823654 (p1 input))

(= 5905 (p2 test-input))
(= 245461700 (p2 input))
