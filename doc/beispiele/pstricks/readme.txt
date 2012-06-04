Wenn pstricks mit pdflatex verwendet werden soll, ist es
am besten, wie folgt vorzugehen:

 -- jede PSTricks-Grafik in ein eigenes Dokument einfügen
 -- dabei das preview-Paket mit der Option psfixbb verwenden
 -- dvips ohne Optionen -E -i aufrufen
 -- mit ps2eps und epstopdf das Bild in das pdf-Format umwandeln
 -- das Bild im eps/pdf-Format einbinden:  includegraphics{bild}
