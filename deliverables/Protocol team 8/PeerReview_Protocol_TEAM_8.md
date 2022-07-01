# Peer-Review 2: Protocollo di Comunicazione

**Niccolò Nicolosi, Emanuele Musto, Francesco Maccari**

Gruppo **9**

Valutazione della documentazione del protocollo di comunicazione del gruppo **8**

## Lati positivi

- Viene mostrato nei sequence diagram un eventuale messaggio di chiusura del gioco durante
  i momenti previsti dalle regole

### Messaggi

- É chiara la specifica di ogni messaggio
- É buona l'idea di utilizzare un unico messaggio moveStudents per muovere tutti gli studenti scelti allo
  stesso tempo, sia sui tavoli che sulle isole, per evitare l'invio di più messaggi. Andrebbe solo un po'
  rivisto per specificare l'isola o le isole sulle quali spostare gli studenti

## Lati negativi

- Mancano le descrizioni dei vari sequence diagram, il che rende più difficile capire come
  sono stati pensati
- Nei sequence diagram viene mostrata l'interazione tra il server e un solo client. Sarebbe
  invece opportuno mostrare l'interazione con almeno due client, in modo da simulare
  correttamente le varie fasi di gioco

### Messaggi

- I messaggi partono tutti dal client, il che presuppone una parte di controller lato client che però
  non è ben specificata nei sequence diagram. Da tenere a mente che data questa struttura, il server dovrà
  necessariamente occuparsi di fare dei controlli ed eventualmente scartare alcuni messaggi non validi o 
  mandati al momento sbagliato
- Mancano i messaggi per la gestione della lobby prima della partita: che messaggio viene mandato ai 
  giocatori che si connettono successivamente al primo client? Il messaggio `EnterGame(gameType, playerNumber)`
  non è adatto poiché contiene gli argomenti che solo il primo client dovrebbe mandare
- Nel messaggio `MoveStudent(toDining, toIsland)` non è chiaro come sia possibile scegliere le isole su 
  cui debbano andare gli studenti. Inoltre utilizzando un messaggio per tutti gli studenti non 
  è possibile indirizzare due studenti su isole diverse, eventualità prevista dalle regole di gioco

### Login

- Dal sequence diagram non si evince che cosa succede dopo che è stato ricevuto un messaggio
  di errore: il server disconnette l'utente e chiude tutte le connessioni (come da specifica)
  o attende nuovi messaggi di login?

### Access

- Non vengono gestiti gli altri client: dal diagram sembra che tutti i client che si connettono
  debbano mandare anche un `gameType` e un `playerNumber`, mentre dovrebbe essere prerogativa
  solamente del primo client che si connette
- Da come è scritto sembra che appena il primo client si connette entra subito in gioco da solo,
  visto il messaggio di `Update(model)`. Non dovrebbe invece attendere gli altri giocatori?
- In caso di messaggio `Error(errorType)`, sembra che il Server mandi comunque al Client il
  messaggio `Update(model)`, cosa che non dovrebbe succedere


### Planning Phase

- Non viene mostrato quello che succede prima della scelta della carta assistente, ovvero il 
  riempimento delle nuvole con studenti presi dal sacchetto. Pur essendo una azione effettuata
  solo lato server, necessita comunque di un messaggio apposito che comunichi l'aggiornamento delle
  nuvole ai client
- Non è ben chiaro il ruolo del messaggio `playAssistant(assistantCard)` che torna dal server al client.
  Non dovrebbe essere un messaggio inviato esclusivamente dal client verso il server? Se l'intento è
  quello di aggiornare gli altri client con la scelta fatta, forse sarebbe più opportuno creare un 
  messaggio apposito

### Action 1

- Come descritto nella sezione *Messaggi* non è possibile secondo il sequence diagram spostare
  due studenti su isole diverse e non è inoltre specificata l'isola sul quale spostare gli studenti
- Non è ben chiaro il ruolo del messaggio `MoveStudents(toDining, toIsland)` che torna dal server al
  client. Non dovrebbe essere un messaggio inviato esclusivamente dal client verso il server? Se 
  l'intento è quello di aggiornare gli altri client con la scelta fatta, forse sarebbe più opportuno
  creare un messaggio apposito
- Non è chiaro cosa debba succedere dopo un eventuale messaggio `Error(errorType)`
- Non viene mostrato quello che succede dopo lo spostamento degli studenti, ovvero l'aggiornamento
  del possesso dei professori. Pur essendo una modifica effettuata solo lato server,
  necessita comunque di un messaggio apposito che la comunichi ai client

### Action 2

- Non è ben chiaro il ruolo del messaggio `MoveMother(step)` che torna dal server al
  client. Non dovrebbe essere un messaggio inviato esclusivamente dal client verso il server? Se
  l'intento è quello di aggiornare gli altri client con la scelta fatta, forse sarebbe più opportuno
  creare un messaggio apposito
- Non è chiaro cosa debba succedere dopo un eventuale messaggio `Error(errorType)`
- Non viene mostrato quello che succede dopo lo spostamento degli studenti, ovvero l'aggiornamento
  del possesso delle isole. Pur essendo una modifica effettuata solo lato server,
  necessita comunque di un messaggio apposito che la comunichi ai client

### Action 3

- Non è ben chiaro il ruolo del messaggio `ChoseCloud(cloudId)` che torna dal server al
  client. Non dovrebbe essere un messaggio inviato esclusivamente dal client verso il server? Se
  l'intento è quello di aggiornare gli altri client con la scelta fatta, forse sarebbe più opportuno
  creare un messaggio apposito
- Non è chiaro cosa debba succedere dopo un eventuale messaggio `Error(errorType)`
- Nonostante sia abbastanza chiaro dal nome quale sia la sua funzione, il messaggio
  `CollectCloud(cloudId)` non è presente nella lista dei messaggi con relativa descrizione

### Game Ending

- Non è stato fatto questo sequence diagram. Sarebbe stato opportuno mostrarlo per capire cosa
  succede dopo il messaggio `EndGame(winner)`, e come viene gestita la disconnessione dei client

## Confronto

Un punto di forza del protocollo del gruppo 8 può essere l'utilizzo di messaggi diversi per ogni tipo di
azione eseguita dal client, il che rende più esplicativi i sequence diagram e probabilmente
più immediata la gestione dei messaggi. La scelta del gruppo 9 è invece ricaduta su un unico messaggio di
update, mandato da server a client, che riporta esclusivamente i cambiamenti da apportare alla view per 
sincronizzarla con il model. Nel caso questa scelta risultasse troppo complessa da gestire, il gruppo 9 potrà
replicare la soluzione adottata dal gruppo 8. Una diversità abbastanza profonda tra i due protocolli è 
l'utilizzo di una parte di controller lato client da parte del gruppo 8, la quale però non si è potuta 
revisionare non essendo ben specificata nella documentazione. Alcune possibili soluzioni ai problemi 
trovati nel protocollo del gruppo 8 sono state riportate nella sezione *Lati negativi*
