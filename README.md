# ORC

Bienvenu dans le dépot du projet __Orc__.

---------
__Sujet__ : Faire un système d'agent BDI dans le langage voulu.
__Mise en place__ : Réalisation d'un système d'agents planificatifs en __Java__.

## Spécificités du projet
<p align="justify">
Nous sommes partis, pour ce projet, sur des agents <b>Orcs<b> évoluants dans une univers en 2D. Dans les parties suivantes, nous détaillons notemment les différentes spécificités qui font de notre projet un projet orienté agent. 
</p>

### Détail des actions de l’orc
- Taper : L’orc est capable d’infliger des dégâts à un orc ennemi. Pour cela, il doit être à portée d’attaque.
- Se déplacer : L’orc peut se déplacer dans l’environnement. Pour cela, il doit préciser dans quelle direction il veut se déplacer.
- Marquer l'environnement : L’orc peut aussi laisser une trace sur le sol. Cette trace est visible par les autres orcs, ce qui leur permet de communiquer sur leur chemin.

### Environnement
<p align="justify">
L’orc ne perçoit qu’une partie de l’environnement. Cet environnement est 2D, continu et il peut contenir des obstacles. Mais l’orc ne perçoit que les choses dans un cercle de vision autour de lui. De plus, ce cercle peut être obstrué par des obstacles.
</p>

L’orc possède donc les informations dans sa zone. Il voit :
- La position des orcs
- Les obstacles
- Les marques
En revanche, il ne voit pas la vie des autres orcs.

### Interactions d’un orc
Les orcs peuvent interagir avec leur environnement. Ils peuvent laisser une marque ou tuer un autre orc.

