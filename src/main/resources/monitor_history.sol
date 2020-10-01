pragma solidity >=0.4.21;

contract CaseMonitor{

    Case[] cases; 
    mapping(bytes16 => uint) caseUuidToIndex; 

    

    //defines a case along with its state
    struct Case {
        bytes16 uuid;
        string caseName;
        bool isStudent;
        uint latestDate;
        uint[] datesHistory;
        CaseState[] statesHistory; 
        CaseState currState;
    }

    //possible case states 
    enum CaseState {
        Undefined,    //case has not been processed
        Accepted,     //case has been accepted
        Rejected,     //case has been rejected
        Paid          //case has been paid 
    }

    //Case currCase;

    function _getCaseIndex(bytes16 _uuid) public view returns (uint) {
        return caseUuidToIndex[_uuid]; 
    }

    function addCase(bytes16 _uuid, string memory _caseName, bool _isStudent, uint _date) public returns(uint){

        //hash the crucial info to get a unique id 
       // bytes32 id = keccak256(abi.encodePacked(_uuid, _caseName, _isStudent, _date)); 

        //require that the case be unique (not already added) 
        require(!caseExists(_uuid));

        // currCase.uuid = _uuid;
        // currCase.caseName = _caseName;
        // currCase.isStudent = _isStudent;
        // currCase.creationDate = _date;
        // currCase.datesHistory.push(_date);
        // currCase.statesHistory.push(CaseState.Undefined);
        // currCase.currState = CaseState.Undefined;

        uint[] memory datesHistory = new uint[](1);
        datesHistory[0] = _date;
        CaseState[] memory statesHistory = new CaseState[](1);
        statesHistory[0] = CaseState.Undefined;

        //add the case 
        //cases.push(currCase);
        cases.push(Case(_uuid, _caseName, _isStudent, _date, datesHistory, statesHistory, CaseState.Undefined)); 
        uint newIndex = cases.length-1;
        caseUuidToIndex[_uuid] = newIndex;
        
        //return the unique id of the new case
        return newIndex;
    }

    function updateCase(bytes16 _uuid, string memory _caseName, bool _isStudent, uint _date, CaseState _state) public {

        require(caseExists(_uuid));
        
        uint index = _getCaseIndex(_uuid);
        Case storage theCase = cases[index];
        
        theCase.caseName = _caseName;
        theCase.isStudent = _isStudent;
        theCase.latestDate = _date;
        theCase.datesHistory.push(_date);
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
        string memory caseName,
        bool isStudent,
        uint creationDate,
        uint[] memory datesHistory,
        CaseState[] memory statesHistory,
        CaseState currState) {
            
        require(caseExists(_uuid));

        Case storage theCase = cases[_getCaseIndex(_uuid)];
        return (theCase.uuid, theCase.caseName, theCase.isStudent, theCase.latestDate, theCase.datesHistory, theCase.statesHistory, theCase.currState); 
        
    }

    function deleteCase(bytes16 _uuid) public {
        require(caseExists(_uuid));

        uint index = _getCaseIndex(_uuid);
        delete cases[index];
    }
}