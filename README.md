# UTF8toBOM
Essentially, this is the source code for a tool that searches for .yml files in a Stellaris mod project, and checks them for their encoding.
Since Stellaris is kind of picky if the encoding of a .yml file is not UTF-8 BOM, we convert every standard UTF-8 encoded .yml to the additional BOM encoding.

No guarantee that this tool does not mess up your mod project, so either use a version control software like git, or make a backup before use. I take absolutely no responsibility on what happens.
