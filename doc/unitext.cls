\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{unitext}[2011/08/01 v1.9 unitext, Werner Struckmann]

\newcommand{\@subjekt}{}
\newcommand{\@institut}{}
\newcommand{\@bib}{plain}
\newcommand{\@din}{din}
\newcommand{\@bibstyle}{}
\newcommand{\@sprache}{}
\newcommand{\@logo}{\includegraphics[scale=0.11]{tu-logo}}

\DeclareOption{bachelorarbeit}{\renewcommand{\@subjekt}{Bachelorarbeit}}
\DeclareOption{projektarbeit}{\renewcommand{\@subjekt}{Projektarbeit}}
\DeclareOption{masterarbeit}{\renewcommand{\@subjekt}{Masterarbeit}}
\DeclareOption{seminar}{\renewcommand{\@subjekt}{Ausarbeitung eines Seminarvortrags}}
\DeclareOption{praktikum}{\renewcommand{\@subjekt}{Praktikumsbericht}}
\DeclareOption{teamprojekt}{\renewcommand{\@subjekt}{Teamprojekt}}
\DeclareOption{studienarbeit}{\renewcommand{\@subjekt}{Studienarbeit}}
\DeclareOption{diplomarbeit}{\renewcommand{\@subjekt}{Diplomarbeit}}
\DeclareOption{bericht}{\renewcommand{\@subjekt}{Informatik-Bericht}}
\DeclareOption{script}{\renewcommand{\@subjekt}{Vorlesungsskript}}
\DeclareOption{dissertationv}{%
  \renewcommand{\@subjekt}{Dissertation}%
  \AtBeginDocument{\renewcommand{\maketitle}{\input{titelseite-vorher}}}%
}
\DeclareOption{dissertationn}{%
  \renewcommand{\@subjekt}{Dissertation}%
  \AtBeginDocument{\renewcommand{\maketitle}{\input{titelseite-nachher}}}%
}

\DeclareOption{titelseite}{\AtBeginDocument{\renewcommand{\titelblatt}{\input{titelseite}}}}
\DeclareOption{cd}{\AtBeginDocument{\renewcommand{\titelblatt}{\input{cd}}}}

\DeclareOption{inf}{\renewcommand{\@institut}{Institute für Informatik}}
\DeclareOption{ips}{\renewcommand{\@institut}{Institut für Programmierung und Reaktive Systeme}}
\DeclareOption{idb}{\renewcommand{\@institut}{Institut für Informationssysteme}}
\DeclareOption{ida}{\renewcommand{\@institut}{Institut für Datentechnik und Kommunikationsnetze}}
\DeclareOption{iti}{\renewcommand{\@institut}{Institut für Theoretische Informatik}}
\DeclareOption{eis}{\renewcommand{\@institut}{Institut für Theoretische Informatik\\%
  Abteilung Entwurf Integrierter Schaltungen}}
\DeclareOption{sse}{\renewcommand{\@institut}{Institut für Software Systems Engineering}}

\DeclareOption{abbrv}{\renewcommand{\@bib}{abbrv}}
\DeclareOption{alpha}{\renewcommand{\@bib}{alpha}}
\DeclareOption{plain}{\renewcommand{\@bib}{plain}}
\DeclareOption{unsrt}{\renewcommand{\@bib}{unsrt}}

\DeclareOption{english}{%
  \AtEndOfClass{%
    \RequirePackage[left]{eurosym}
  }
  \AtBeginDocument{%
    \renewcommand{\keywordsname}{Keywords}
    \renewcommand{\glossaryname}{Glossary}
    \renewcommand{\symbolname}{Symbols}
    \renewcommand{\abkuerzungname}{List of Abbreviations}
    \renewcommand{\zahlname}{Numbers}
    \renewcommand{\@bibstyle}{\@bib}
    \selectlanguage{USenglish}
  }
}
\DeclareOption{german}{%
  \AtEndOfClass{%
    \RequirePackage[right]{eurosym}
  }
  \AtBeginDocument{%
    \renewcommand{\@bibstyle}{\@bib\@din}
  }
}

\DeclareOption{pdf}{%
  \AtEndOfClass{%
   \RequirePackage[pdftex]{color}
   \RequirePackage[pdftex]{graphicx}
   \RequirePackage[pdftex,pagebackref,plainpages=false,pdfpagelabels,bookmarksnumbered,
     colorlinks,urlcolor=black,linkcolor=black,citecolor=black]{hyperref}
   \pdfstringdefDisableCommands{\let\and~}
  }
}

\DeclareOption{ps}{%
  \AtEndOfClass{%
   \RequirePackage[dvips]{color}
   \RequirePackage[dvips]{graphicx}
   \RequirePackage[dvips,pagebackref,plainpages=false,pdfpagelabels,bookmarksnumbered,
     colorlinks,urlcolor=black,linkcolor=black,citecolor=black]{hyperref}
  }
}

\DeclareOption{dvi}{%
  \AtEndOfClass{%
   \RequirePackage[xdvi]{color}
   \RequirePackage[xdvi]{graphicx}
   \RequirePackage[hypertex,pagebackref,plainpages=false,pdfpagelabels,bookmarksnumbered,
     colorlinks,urlcolor=black,linkcolor=black,citecolor=black]{hyperref}
  }
}

\ExecuteOptions{inf,plain}
\ProcessOptions\relax

\LoadClass[a4paper,12pt,twoside,DIV12,BCOR08mm]{scrreprt}
\KOMAoptions{abstract=on,cleardoublepage=empty,numbers=noenddot,
  headsepline=false,headings=normal,listof=totoc,index=totoc,bibliography=totoc}
% \setkomafont{pagehead}{\sffamily\bfseries}

\RequirePackage[latin1]{inputenc}

\RequirePackage[T1]{fontenc}
\RequirePackage{textcomp}
\RequirePackage{lmodern}
\RequirePackage{amssymb}
\RequirePackage{amsmath}

\RequirePackage{makeidx}
\RequirePackage{multicol}
\RequirePackage{ifthen}
\RequirePackage{xspace}
\RequirePackage[thinspace,thinqspace,squaren,textstyle]{SIunits}
\RequirePackage{ngerman}

\renewcommand*{\partpagestyle}{empty}
\renewcommand*{\chapterformat}{\chapappifchapterprefix{\ }\thechapter\enskip}

\newcommand{\@uni}{{\sffamily\bfseries%
\makebox[\textwidth]{TECHNISCHE UNIVERSIT\"AT CAROLO-WILHELMINA ZU BRAUNSCHWEIG}}}

\titlehead{%
  \hfill\@uni\hfill
  \pdfbookmark[0]{Titel}{titelblatt}
}

\newcommand{\@nummer}{}
\newcommand{\nummer}[1]{\renewcommand{\@nummer}{~#1}}

\renewcommand*{\subject@font}{\normalfont\normalcolor\mdseries\Large}
\subject{{\sffamily\@subjekt\@nummer}}

\newcommand\titelblatt{{\sffamily\maketitle}}

\newcommand{\@dozent}{}
\newcommand{\dozent}[1]{\renewcommand{\@dozent}{#1}}

\newcommand{\@betreuer}{}
\newcommand{\betreuer}[2][betreut durch:]{%
  \renewcommand{\@betreuer}{#1\par#2}
}

\newcommand{\@keywords}{}
\newcommand{\keywords}[1]{\renewcommand{\@keywords}{#1}}

\publishers{%
  \enlargethispage{10mm}
  \vskip08mm\@logo\par
  {\sffamily\normalsize\vskip15mm\@institut\par\@dozent\par\vskip08mm\@betreuer}
}

\newcommand{\erklaerungname}{Eidesstattliche Erklärung}
\newcommand{\erklaerung}{%
  \cleardoublepage
  \pdfbookmark[0]{\erklaerungname}{erklaerungname}
  \thispagestyle{empty}
  \hskip 0mm
  \vfill
  \begin{center}\sffamily\bfseries\large
    \erklaerungname
  \end{center}
  \bigskip\noindent Hiermit erkläre ich an Eides statt, dass
  ich die vorliegende Arbeit selbstständig verfasst und keine
  anderen als die angegebenen Hilfsmittel verwendet habe.\par
  \bigskip\noindent Braunschweig,~\datum\par
  \vskip 10mm
  \hfill\hrulefill
}

\renewcommand{\prefacename}{Vorwort}
% \AtBeginDocument{\renewcommand{\prefacename}{Foreword}}
\newcommand{\vorwort}{%
  \cleardoublepage
  \chapter*{\prefacename}
  \pdfbookmark[0]{\prefacename}{prefacename}
  \markboth{\prefacename}{\prefacename}
  \InputIfFileExists{vorwort}{}{}
}

\renewcommand{\abstractname}{{\normalsize Zusammenfassung}}
\newcommand{\keywordsname}{Stichwörter}
\newcommand{\zusammenfassung}
  {\begin{abstract}
     \markboth{\abstractname}{\abstractname}
     \pdfbookmark[0]{\abstractname}{abstractname}
     \noindent
     \InputIfFileExists{zusammenfassung}{}{}\par\bigskip\noindent
     \ifthenelse{\equal{\@keywords}{}}{}
      {{\small{\sffamily\bfseries\keywordsname}\quad\@keywords}}
   \end{abstract}
  }

\renewcommand{\contentsname}{Inhaltsverzeichnis}
\let\orig@toc\tableofcontents
\newcommand{\my@toc}{%
  \cleardoublepage
  \pdfbookmark[0]{\contentsname}{contentsname}
  \orig@toc
}
\let\tableofcontents\my@toc

\renewcommand{\listfigurename}{Verzeichnis der Abbildungen}

\renewcommand{\listtablename}{Verzeichnis der Tabellen}

\newcommand{\abkuerzungname}{Verzeichnis der Abkürzungen}

\newcommand{\abkuerzung}{%
  \newpage
  \chapter*{\abkuerzungname}
  \markboth{\abkuerzungname}{\abkuerzungname}
  \addcontentsline{toc}{chapter}{\abkuerzungname}
  \InputIfFileExists{abkuerzung}{}{}
}

\newcommand{\starttext}{%
  \cleardoublepage
  \pagenumbering{arabic}
}

\renewcommand{\appendixname}{Anhang}
\newcommand{\anhang}{%
  \begin{appendix}
    \InputIfFileExists{anhang}{}{}
  \end{appendix}
}

\bibliographystyle{\@bibstyle}
\renewcommand{\bibname}{Literatur}

\newcommand{\glossaryname}{Glossar}
\newenvironment{theglossary}
  {\chapter*{\glossaryname}
   \markboth{\glossaryname}{\glossaryname}
   \addcontentsline{toc}{chapter}{\glossaryname}
   \setlength{\parindent}{0pt}
   \setlength{\parskip}{0pt plus 0.4pt}
   \begin{description}}
  {\end{description}}
\newcommand{\glossar}[2]{\glossary{[#1]\quad#2|hyperpage}}
\newcommand{\printglossary}{\@input@{\jobname.gls}}

\newcommand{\symbolname}{Symbole}
\newcommand{\zahlname}{Zahlen}
\renewcommand{\indexname}{Sachwortregister}
\renewenvironment{theindex}
  {\chapter*{\indexname}
   \markboth{\indexname}{\indexname}
   \addcontentsline{toc}{chapter}{\indexname}
   \vspace*{-10pt}
   \setlength{\parindent}{0pt}
   \setlength{\parskip}{0pt plus 0.4pt}
   \setlength{\columnsep}{35pt}
   \setlength{\columnseprule}{0.0pt}
   \renewcommand{\item}{\@idxitem}
   \begin{multicols}{2}}
  {\end{multicols}}

\renewcommand{\theenumi}{\arabic{enumi}}
\renewcommand{\theenumii}{\alph{enumii}}
\renewcommand{\labelenumi}{\theenumi.}
\renewcommand{\labelenumii}{\theenumii)}
\renewcommand{\labelitemi}{\textbullet}
\renewcommand{\labelitemii}{\textopenbullet}

\DeclareRobustCommand{\xdot}{\@ifnextchar.{}{.\xspace}}

\pagestyle{headings}
\pagenumbering{roman}

\InputIfFileExists{unitext.cfg}{}{}
\InputIfFileExists{glossar.tex}{}{}

\setlength{\overfullrule}{4pt}

\raggedbottom

\AtBeginDocument{%
  \let\datum\@date
  \hypersetup{%
    pdftitle={\@title},
    pdfauthor={\@author},
    pdfsubject={\@subject},
    pdfkeywords={\@keywords},
    pdfcreator={\LaTeX{} with local class \dq{}unitext\dq{}
      (based on \dq{}scrreprt\dq) and package \dq{}hyperref\dq},
  }
}

\endinput
