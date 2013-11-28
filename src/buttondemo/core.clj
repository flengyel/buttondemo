(ns buttondemo.core
   (:gen-class)
   (:import [javax.swing AbstractButton JButton JPanel JFrame ImageIcon]
            [java.awt.event ActionEvent KeyEvent ActionListener])
   (:use [clojure.contrib.swing-utils]))

(defn create-image-icon [path]
; (prn (. System getProperty "user.dir"))
; (prn (. System getProperty "java.class.path"))
   (if-let [img-url (clojure.java.io/resource path)]
      (ImageIcon. img-url)
      (.println System/err (str "File not found:" path))))

(defn enable-buttons [button-flags]
	(doseq [[button flag] button-flags] (.setEnabled button flag))) 

(defn initialize-buttons [b1 b2 b3]
   (doto b1
	  (add-action-listener 
          (fn [_] (enable-buttons [[b1 false][b2 false][b3 true]])))
      (.setVerticalTextPosition AbstractButton/CENTER)
      (.setHorizontalTextPosition  AbstractButton/LEADING)
      (.setToolTipText "I can disable the middle button.")
      (.setMnemonic  KeyEvent/VK_D))
   (doto b2
      (add-action-listener (fn [_] (prn "I told you not to click me.")))
      (.setVerticalTextPosition AbstractButton/BOTTOM)
      (.setHorizontalTextPosition AbstractButton/CENTER)
      (.setToolTipText "Don't click me.")
      (.setMnemonic KeyEvent/VK_M))
   (doto b3
      (add-action-listener 
         (fn [_] (enable-buttons [[b1 true][b2 true][b3 false]])))
      (.setMnemonic KeyEvent/VK_E)
      (.setToolTipText "I can enable the middle button.")
      (.setEnabled false))
)

(defn -main [] 
  (let [[b1 b2 b3 :as buttons] (for [[title file] 
	;	 [["<html><center><b><u>D</u>isable</b><br><font color=#ffffdd>middle button</font>" "right.gif"]
                                     [["Disable middle button" "right.gif"]
                                     ["Middle button" "middle.gif"]
                                     ["Enable middle button" "left.gif"]]]
                                    (JButton. title (create-image-icon file)))
	    panel (doto (JPanel.) (.setOpaque true))]

	 (doseq [b buttons] (doto panel (.add b)))

     (initialize-buttons b1 b2 b3)

	 (do-swing-and-wait
        (doto (JFrame. "Button Demo") 
          (.setDefaultCloseOperation  JFrame/EXIT_ON_CLOSE)	
          (.setContentPane panel)
          (.pack)
          (.setVisible true))))
)
