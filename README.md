# Baking App - Let's Bake
<h3>Description</h3>

An Android application to show baking procedures: ingredients, instructions and videos.

<h4>Recipe Cards</h4>
The main page shows the recipe cards that shows the four recipe names: Nutella Pie, Brownies, Yellow Cake, and Cheesecake.<br/>

<img src="https://drive.google.com/uc?export=view&id=1RYulWRVrk25EK-LDa2DcTWMckIqhPo_Z" width="48%" height="48%">

<h4>Recipe Details</h4>
The screen shows the ingredients and steps for each recipe. The list of ingredients is a scrollable view.<br/>
<img src="https://drive.google.com/uc?export=view&id=1_Ngpf_xQmpsYmmD6uypCjOkmIisbxt8N" width="48%" height="48%">

To see the details of the recipe step, click on any of the cards shown under <b>Recipe Steps</b>

<h4>Recipe Step Details</h4>
The screen shows the details of the recipe step, including a video (implemented using Android library ExoPlayer) and a description.
At the bottom are two buttons to go to the Next or Previous steps. <br/>

<img src="https://drive.google.com/uc?export=view&id=1eRbDb9vrrzLvL2TD1eBZ_8o8DqZslTQ1" width="48%">

You can also view the video in full screen by tilting it to landscape mode on a phone.<br/>

<img src="https://drive.google.com/uc?export=view&id=1Q6gQc7gevPz3TuVxDRaWFst1QZOjdM-g" width="48%">

<h3>Libraries Used</h3>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://github.com/google/ExoPlayer">ExoPlayer </a>for displaying in-app videos<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Espresso for conducting UI tests<br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://github.com/google/volley">Volley </a>for fetching data from internet asynchronously<br/>

<h3>Permissions Required</h3>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;INTERNET <br/>
    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;ACCESS_NETWORK_STATE
