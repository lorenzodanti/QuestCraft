# ⚔️ QuestCraft

---
QuestCraft è un videogioco RPG a griglia 2D sviluppato in Java e JavaFX.
Il giocatore interpreta un eroe che deve farsi strada all'interno di un dungeon su una mappa 5x5, affrontando nemici (Goblin, Orchi e un Boss finale) e gestendo le proprie risorse di cura per raggiungere la casella di vittoria.

Il progetto è stato realizzato applicando i principi della programmazione orientata agli oggetti (OOP),
il pattern architetturale MVC (Model-View-Controller), i principi SOLID e la persistenza dei dati tramite formato JSON.

Autore: Lorenzo Danti  
Matricola: 126280  
Esame: Progetto sviluppato per l'esame di Metodologie di Programmazione e Gestione del Codice (MPGC)  
Anno accademico: 2025/26 - Università di Camerino

## 🎮 Caratteristiche principali

---
- ✅ **Mappa esplorabile (5x5):** Mappa a griglia con posizionamento dinamico di nemici, casella di partenza (in alto a sinistra) e obiettivo finale (in basso a destra).
- ✅ **Sistema di combattimento dedicato:** Finestra popup scontro a turni con opzioni per **Attaccare** o **Difendersi** (-50% di danno subito).
- ✅ **Gestione risorse (Inventario):** Utilizzo limitato e consumabile di pozioni di cura (+25 PV) integrate direttamente tramite la classe `Inventory`.
- ✅ **Progressione eroe:** Guadagno di Punti Esperienza (EXP) e aumento di livello dell'eroe dopo ogni vittoria contro un nemico.
- ✅ **Classifica e Persistenza JSON:** Salvataggio e caricamento automatizzato dello stato di gioco (`savegame.json`) e classifica storica (`leaderboard.json`) che traccia i punteggi, i mostri sconfitti e il livello raggiunto.
- ✅ **Interfaccia grafica & Tile Set:** Grafica personalizzata JavaFX in stile "Dark Mode Gaming" con icone PNG trasparenti per eroe, mostri e obiettivo.
- ✅ **Controlli per movimento (WASD/frecce):** Event Filter globale JavaFX per garantire un movimento fluido dell'eroe sia tramite tasti direzionali che tasti WASD.

## 🛠️ Tecnologie utilizzate

---
-  **Java 17 / JDK 25** (LTS)
-  **JavaFX** (UI, Layouts, Custom Event Filtering)
-  **Gson** (Serializzazione e deserializzazione JSON)
-  **Gradle** (Gestione delle dipendenze e build automation)
-  **JUnit 5** (Suite di test unitari automatizzati)

## 🚀 Come eseguire il progetto

---
### Prerequisiti
- **JDK 17+ JDK 25**
- **Gradle**

### 1 - Istruzioni
```bash
git clone https://github.com/lorenzodanti/QuestCraft.git
cd QuestCraft
```

### 2 - Build del progetto
```bash
./gradlew build
```

### 3 - Esecuzione
```bash
./gradlew run
```

## 🤖 Uso di strumenti di AI

---

### 1 - ChatGPT (OpenAI) utilizzato per:

    -Comprensione concetti teorici (pattern MVC, gestione eventi)

### 2- GitHub Copilot (OpenAI) utilizzato per:

    -Autocomletamento di metodi ripetitivi
    -Generazione rapida di codice

### 3 - Gemini (Google) utilizzato per:

    -Chiarire errori di compilazione (classi mancanti, import)
    -Suggerimenti su struttura del codice e organizzazione delle classi
    -Debug e risoluzione di problemi

L'AI è stata utilizzata solo come supporto e per la risoluzione di problemi, tutto il codice suggerito è stato analizzato,
testato, modificato e integrato manualmente dall'autore del progetto affinché rispettasse le specifiche del progetto.

⚠️ **NOTA BENE!**  
Progetto universitario sviluppato per l'esame di Programmazione Avanzata / Modellazione e Gestione della Conoscenza (UNICAM).

---
