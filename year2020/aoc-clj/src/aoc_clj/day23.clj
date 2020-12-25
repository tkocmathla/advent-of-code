(ns aoc-clj.day23)

(def test-input [3 8 9 1 2 5 4 6 7])
(def input [1 8 6 5 2 4 9 7 3])

(defn dest [cups cup picks]
  (cond
    (zero? cup) (recur cups (apply max cups) picks)
    (picks cup) (recur cups (dec cup) picks)
    :else cup))

(defn shift [cups]
  (let [[pre [_ & post]] (split-with (complement #{1}) cups)]
    (concat post pre)))

(defn p1 [cups]
  (->> cups
       (iterate
         (fn [[cup x y z & xs]]
           (let [i (dest cups (dec cup) #{x y z})
                 [pre [_ & post]] (split-with (complement #{i}) xs)]
             (concat pre [i x y z] post [cup]))))
       (#(nth % 100))
       shift
       (apply str)))

(comment
  (= "67384529" (p1 test-input))
  (= "45983627" (p1 input))
  (= (p2 test-input))
  (= (p2 input)))
