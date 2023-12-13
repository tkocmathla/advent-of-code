(ns aoc-clj.util)

(def ^:dynamic *debug* false)
(defn dump [& s] (when *debug* (apply prn s)))

(defn queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll]
   (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(def str-int #(Long/parseLong %))
