pragma solidity >=0.4.21;

contract CaseMonitor{

    Case[] cases; 
    mapping(bytes16 => uint) caseUuidToIndex; 

    

    //defines a case along with its state
    struct Case {
        bytes16 uuid;
        uint latestDate;
        uint[] datesHistory;
        CaseState[] statesHistory;
        CaseState currState;
        uint[] paymentDateHistory;
        uint[] paymentValueHistory;
        uint paymentOffset;
    }

    //possible case states
    enum CaseState {
        Undefined,    //case has not been processed
        Accepted,     //case has been accepted
        Rejected,     //case has been rejected
        Paid,         //case has been paid
        Paused        //case is paused
    }

    //Case currCase;

    function _getCaseIndex(bytes16 _uuid) public view returns (uint) {
        return caseUuidToIndex[_uuid];
    }

    function addCase(bytes16 _uuid, uint _date) public returns(uint){

        //hash the crucial info to get a unique id
       // bytes32 id = keccak256(abi.encodePacked(_uuid, _caseName, _isStudent, _date));

        //require that the case be unique (not already added)
        require(!caseExists(_uuid));

        uint[] memory datesHistory = new uint[](1);
        datesHistory[0] = _date;
        CaseState[] memory statesHistory = new CaseState[](1);
        statesHistory[0] = CaseState.Undefined;
        uint[] memory paymentDateHistory = new uint[](1);
        paymentDateHistory[0] = 0;
        uint[] memory paymentHistory = new uint[](1);
        paymentHistory[0] = 0;

        //add the case
        //cases.push(currCase);
        cases.push(Case(_uuid, _date, datesHistory, statesHistory, CaseState.Undefined, paymentDateHistory, paymentHistory, 0));
        uint newIndex = cases.length-1;
        caseUuidToIndex[_uuid] = newIndex;

        //return the unique id of the new case
        return newIndex;
    }

    function updateCase(bytes16 _uuid, uint _date, CaseState _state, uint _offset) public {

        require(caseExists(_uuid));

        uint index = _getCaseIndex(_uuid);
        Case storage theCase = cases[index];

        theCase.latestDate = _date;
        theCase.datesHistory.push(_date);
        theCase.statesHistory.push(_state);
        theCase.currState= _state;
        theCase.paymentOffset = _offset;

    }

    function addPayment(bytes16 _uuid, CaseState _state, uint _pDate, uint _payHistory) public{

        require(caseExists(_uuid));

        uint index = _getCaseIndex(_uuid);
        Case storage theCase = cases[index];

        if(theCase.paymentDateHistory[0] == 0){
            theCase.paymentDateHistory[0] = _pDate;
            theCase.paymentValueHistory[0] = _payHistory;
        } else{
            theCase.paymentDateHistory.push(_pDate);
            theCase.paymentValueHistory.push(_payHistory);
        }

        theCase.paymentOffset = 0;
        theCase.latestDate = _pDate;
        theCase.datesHistory.push(_pDate);
        theCase.statesHistory.push(_state);
        theCase.currState= _state;

    }

    function caseExists(bytes16 _uuid) public view returns (bool) {
        if (cases.length == 0)
            return false;

        if(cases[_getCaseIndex(_uuid)].uuid == _uuid){
            return true;
        }
    }

    function getAllCases() public view returns (bytes16[] memory) {
        bytes16[] memory output = new bytes16[](cases.length);

        //get all ids
        if (cases.length > 0) {
            uint index = 0;
            for (uint n = cases.length; n > 0; n--) {
                output[index++] = cases[n-1].uuid;
            }
        }

        return output;
    }

    function getCase(bytes16 _uuid) public view returns (
        bytes16 uuid,
        uint creationDate,
        uint[] memory datesHistory,
        CaseState[] memory statesHistory,
        CaseState currState,
        uint[] memory paymentDateHistory,
        uint[] memory paymentValueHistory,
        uint paymentOffset) {

        require(caseExists(_uuid));

        Case storage theCase = cases[_getCaseIndex(_uuid)];
        return (theCase.uuid, theCase.latestDate,
                 theCase.datesHistory, theCase.statesHistory, theCase.currState,
                 theCase.paymentDateHistory, theCase.paymentValueHistory, theCase.paymentOffset);

    }

    function deleteCase(bytes16 _uuid) public {
        require(caseExists(_uuid));

        uint index = _getCaseIndex(_uuid);
        if(index != cases.length - 1){
            // switch the case to be deleted with the last case of the array and then pop it
            Case memory caseToDelete = cases[index];
            cases[index] = cases[cases.length - 1];
            cases[cases.length - 1] = caseToDelete;
        }

        cases.pop();
    }
}