# Peer-Review 1: UML

**Niccolò Nicolosi, Emanuele Musto, Francesco Maccari**

Gruppo **9**

Valutazione del diagramma UML delle classi del gruppo **8**.

## Lati positivi

### Note Generali:

- Apprezzabile la modellizzazione degli elementi del gioco che non ricalca necessariamente il gioco da tavolo fisico.
- L'utilizzo delle enumerazioni rende comprensibile e ordinato il diagramma.

### `Team`:

- Buona idea quella di mantenere tutte le informazioni relative ad ogni team (es: numero di torri, professori) nelle 
  rispettive istanze della classe `Team`, nonostante nel gioco da tavolo torri e professori appartengano al singolo giocatore
	
### `DominanceCalculator`:

- Ottimo l'utilizzo del pattern *Strategy* per il calcolo della dominanza.

## Lati negativi

### Note Generali:

- Attenzione ai versi delle frecce e alla posizione delle cardinalità, poiché nello standard UML sono da riportare 
  invertite rispetto al diagramma ER (stando a quanto appreso nel corso di Basi di Dati).
- Attenzione a non restituire un riferimento ad attributi privati nei metodi getters, poiché ciò non rispetterebbe 
  i principi della programmazione *Object Oriented*. Nel vostro diagramma ci sono infatti molti esempi di metodi che
  resituiscono array: fare attenzione a restituirne una copia.
- Utilizzo esagerato di array. Si potrebbero sfruttare maggiormente le *Collections* offerte da Java, dati i vantaggi
  di avere dimensione dinamica e di disporre di metodi utili alla gestione.
- Sono descritte alcune classi di grandi dimensioni che astraggono più entità assieme. Ad esempio, nella classe `Player`,
  oltre all'entità player, si modellizza anche la plancia scuola. È buona prassi creare più classi di dimensioni ridotte
  che si occupino di gestire entità specifiche.
- Potrebbero essere aggiunti alcuni design pattern là dove utili. (es: *Factory*, *Decorator*)

### `StudentBuffer`:

- La classe `StudentBuffer` svolge la stessa funzione di una mappa (`Map<Color, Integer>`) già definita dalle librerie di 
  Java. Per definire funzionalità aggiuntive la mappa può essere decorata da classi più specifiche. Ad esempio 
  l'estrazione di uno studente di un colore casuale, necessaria per pescare dal sacchetto degli studenti, può essere
  definita in una classe `StudentsBag`, che contenga come attributo la mappa semplice.
- La corrispondenza riportata tra `StudentBuffer` e `Island` è di 1 a 1, ma un'istanza di `StudentBuffer` può essere usata
  per rappresentare anche il sacchetto degli studenti o una nuvola: in quel particolare caso il buffer non è associato
  a nessuna isola.

### `TeamColor`:

- Il campo `EMPTY` non è necessario, poiché a ogni team corrisponde uno specifico colore di torre.

### `AssistantCard`:

- I metodi setters sono superflui, poichè gli attributi `turnNumber` e `motherNumber` non cambiamo nel corso della partita.

### `Round`:

- La classe `Round` viene usata per astrarre attraverso diverse istanze, rispettivamente `currentRound` e `nextRound`,
  la planning phase e la action phase del turno. Per l'utilizzo che ne si fa, essa non modellizza propriamente il
  concetto di round. Questo genera confusione e rende sicuramente più difficile la manutenzione e l'estensione del
  software. Il problema può essere risolto semplicemente usando un'unica variabile `currentRound`, modificandone lo stato.
- L'utilizzo di diversi array per memorizzare i player in gioco e le carte giocate da ognuno di essi risulta scomodo
  da gestire, in quanto bisogna assicurarsi di mantenere manualmente la corrispondenza tra indici. Lo stesso vale per
  le azioni eseguite da ogni player. Anche in questo caso è consigliabile utilizzare le mappe di Java (`Map<Player, AssistantCard>`
  e `Map<Player, PlayerAction>`) a tale scopo, salvando i player in gioco in una lista (`List<Player>`). Tale approccio
  risulta infatti più chiaro e intuitivo.
- Tenere presente che la classe `Round`, essendo parte del model, deve contenere solamente i dati riguardanti il turno:
  la logica di svolgimento del turno dovra infatti essere gestita interamente dal controller per rispettare il pattern MVC

### `CharacterCard`:

- `CharacterCard` è dichiarata come classe astratta ed è estesa dalle classi `Character1` e `Character2`, entrambe vuote.
  Che ruolo hanno queste classi? In cosa si differeziano? Una classe vuota non aggiunge informazioni al diagramma.

## Confronto tra le architetture

Dal confronto tra le architetture emerge che quella realizzata dal gruppo 8 è in un certo senso più vicina alla rappresentazione su macchina,
mentre quella realizzata dal gruppo 9 cerca di essere più fedele al gioco fisico, astraendo ogni suo componente più nello specifico. Entrambi
gli approcci hanno i loro vantaggi: il primo può essere più efficiente e meno dispersivo, mentre il secondo può perdere in effiecienza ma 
risulta più intuitivo per chi sviluppa o estende il software. Ciò si riflette anche sulla struttura delle classi che, nel primo caso accorpano
diverse funzionalità risultando dunque più grandi, mentre nel secondo si occupano di funzionalità molto specifiche avendo dimensioni più ridotte.
Un lato positivo di entrambi i diagrammi è il largo uso di enumerazioni, che facilitano molto la lettura e comprensione del codice. Una nota 
di merito da riconoscere al gruppo 8 è l'utilizzo del pattern *Strategy*, che semplifica notevolmente il calcolo della dominanza di isola, il
quale dipende da molte condizioni. Dunque il gruppo 9 adotterà sicuramente la stessa soluzione.