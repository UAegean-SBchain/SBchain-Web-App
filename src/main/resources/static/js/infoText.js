const infoButtons = document.getElementsByClassName("infoButton");
const infoTextBoxes = document.getElementsByClassName("infoTextBox");

for(let i=0; i< infoButtons.length; i++){
    infoButtons[i].addEventListener('click', function(){
        infoTextBoxes[i].style.display = infoTextBoxes[i].style.display === 'none' ? 'block' : 'none';
    });
}