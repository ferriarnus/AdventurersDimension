# Adventurers Dimension
A mod for all those who want to keep exploring! 

## What does it do?
Adventurers Dimension allows for the creation of new dimensions based on existing (modded) dimensions.
These dimension are defined using a recipe and are accessible in game using the adventurers workbench and compass.
This means that for servers, where the resource have already been plundered by others, a new dimension can be made with need structures to explore!
Has the end been fully cleared out? No worries! Simply make an adventurers dimension with the end dimension as target, and a new untouched dimension is made!
Or in case you are on a server with a world border and have added a new mod that adds worldgen feature? Make a new dimension and you will be able to visit the new biomes, find the new ores and visit the new structures!
And all that without messing with the existing dimension or world border!
After a certain amount of time, the dimension will delete itself, and will create a new, clean one.

## How to use:
Make a new (or edit the existing) dimension recipe: 
```json
{
  "type": "adventurersdimension:dimension",
  "input": [
    {
      "item": "minecraft:grass_block"
    }
  ],
  "dimension": "minecraft:overworld",
  "player": false,
  "time": 1
}
```
``type`` is the recipe type. This needs to be `adventurersdimension:dimension`.  
``input`` is the input item that determines the recipe. This uses the default minecraft input syntax, so it can be a tag as well.  
``dimension`` is the dimension to copy.  
``player`` means if the dimension should be prefixed with a player name. This would mean that each player cam make its own dimension. Do not that they are not private.  
``time`` is the time in minecraft days (20 minutes) that the dimension will exist. After that time, the dimension will delete itself. It can be remade afterwards, but it will be a completely new dimension with a different seed. If the time is set to `-1`, the dimension will not go away.  
  
After setting up the recipe, you need to make an Adventurers Workbench and put in the right item for the dimension. After you've selected the dimension, you will be able to teleport to it using the Adventurers Compass. 
With shift right-clicking with the compass, you are than transported to the new dimension. To return, you simply use and hold the compass and it will bring you back to the workbench you used to enter.

## Config
Currently, there is one config entry, `Permission`. This entry determines what (server) permission level a user needs to have to be able to set the dimension. This setting is so server operators can set op a workbench with a dimension, without the risk of users changing it. 
