(ns aoc-clj.day22
 (:require
    [clojure.java.io :as io]
    [clojure.pprint :refer [pprint]]
    [clojure.string :as string]))

(defn parse [file] (->> file io/resource slurp string/split-lines))
(defonce test-input (parse "day22_test.txt"))
(defonce input (parse "day22.txt"))

; ------------------------------------------------------------------------------

(defn queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll] (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(defn decks [lines]
  (let [[_ s1 _ s2] (map vec (partition-by #(string/includes? % "Player") (remove #{""} lines)))]
    [(queue (map read-string s1))
     (queue (map read-string s2))]))

(defn score [[q1 q2]]
  (let [q (or (seq q1) (seq q2))]
    (reduce + (map * q (range (count q) 0 -1)))))

(defn p1 [lines]
  (->> (decks lines)
       (iterate
         (fn [[q1 q2]]
           (let [c1 (peek q1), c2 (peek q2)]
             (if (> c1 c2)
               [(conj (pop q1) c1 c2) (pop q2)]
               [(pop q1) (conj (pop q2) c2 c1)]))))
       (drop-while (partial every? seq))
       first
       score))

; ------------------------------------------------------------------------------

(declare play)

(defn turn [[[q1 q2] seen]]
  (let [c1 (peek q1), c2 (peek q2), q1' (pop q1), q2' (pop q2)]
    (cond
      (seen [q1 q2]) [[q1 nil]]

      (and (>= (count q1') c1) (>= (count q2') c2))
      (let [[[sq1 sq2]] (play [[(queue (take c1 q1')) (queue (take c2 q2'))] #{}])]
        [[(if (seq sq1) (reduce conj q1' [c1 c2]) q1')
          (if (seq sq2) (reduce conj q2' [c2 c1]) q2')]
         (conj seen [q1' q2'])])

      (> c1 c2) [[(conj q1' c1 c2) q2'] (conj seen [q1 q2])]
      :else     [[q1' (conj q2' c2 c1)] (conj seen [q1 q2])])))

(defn play [args]
  (->> (iterate turn args)
       (drop-while (comp (partial every? seq) first))
       first))

(defn p2 [lines]
  (score (first (play [(decks lines) #{}]))))

(comment
  (= 306 (p1 test-input))
  (= 34566 (p1 input))
  (= 291 (p2 test-input))
  (= 31854 (p2 input)))
