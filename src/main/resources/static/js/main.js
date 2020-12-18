const falseBoxes = document.getElementsByClassName("falseBox");
const trueBoxes = document.getElementsByClassName("trueBox");
const mainBoxes = document.getElementsByClassName("mainBox");
const cbFalseContainer = document.getElementsByClassName("checkboxContainer--false");
const cbTrueContainer = document.getElementsByClassName("checkboxContainer--true");

for(let i=0; i< falseBoxes.length; i++){
    falseBoxes[i].addEventListener('click', function(){
        if(this.checked == true){
            mainBoxes[i].value = 'false';
            trueBoxes[i].checked = false;
            cbFalseContainer[i].style.backgroundColor = "lightgrey";
            cbTrueContainer[i].style.backgroundColor = "rgb(226, 226, 226)";
            
        }

        if(this.checked == false){
            mainBoxes[i].value = 'true';
            trueBoxes[i].checked = true;
            cbFalseContainer[i].style.backgroundColor = "rgb(226, 226, 226)";
            cbTrueContainer[i].style.backgroundColor = "lightgrey";
        }
    });
}

for(let j=0; j< trueBoxes.length; j++){
    trueBoxes[j].addEventListener('click', function(){
        if(this.checked == true){
            mainBoxes[j].value = 'true';
            falseBoxes[j].checked = false;
            cbFalseContainer[j].style.backgroundColor = "rgb(226, 226, 226)";
            cbTrueContainer[j].style.backgroundColor = "lightgrey";
        }

        if(this.checked == false){
            mainBoxes[j].value = 'false';
            falseBoxes[j].checked = true;
            cbFalseContainer[j].style.backgroundColor = "lightgrey";
            cbTrueContainer[j].style.backgroundColor = "rgb(226, 226, 226)";
        }
    });
}

for(let n=0; n< mainBoxes.length; n++){
    if(mainBoxes[n].value == 'true'){
        trueBoxes[n].checked = true;
        falseBoxes[n].checked = false;
        cbFalseContainer[n].style.backgroundColor = "rgb(226, 226, 226)";
        cbTrueContainer[n].style.backgroundColor = "lightgrey";
    }

    if(mainBoxes[n].value == 'false'){
        trueBoxes[n].checked = false;
        falseBoxes[n].checked = true;
        cbFalseContainer[n].style.backgroundColor = "lightgrey";
        cbTrueContainer[n].style.backgroundColor = "rgb(226, 226, 226)";
    }
}

