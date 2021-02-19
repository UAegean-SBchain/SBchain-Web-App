# SBchain Social Solidarity Income Submission Web App #

The Social Solidarity Income Submission Web App, is a web application that an applicant can access and submit applications in order to apply for the benefits of the Social Solidarity Income programme. The transferring of the applicants details is made using Verifiable Credentials, issued from an authoritative (for the purposes of the project) source. The application is then validated on submission in its entirety, through a series of checks, and is either accepted or rejected. As a result, the aforementioned service is designed to provide the following main functionalities:

* It acts as the main point of entry for the applicants in the system, allowing them to submit new applications and review the status of their older applications and guides them through the process.
* It is capable of authorizing the issuance of a special credential, denoting that its holder is entitled to enjoy special perks reserved by various services for socially vulnerable individuals (for example, enjoy a free meal), without revealing any personal identification information to the service provider. Essentially shielding them from social discrimination.

In more details, by using its web interfaces the applicants are able to submit Verifiable Credentials (VCs) issued to them by and stored in a special mobile phone application to fill in the required information, in order to apply for the benefit. The consumption of these VCs is designed to take place over OpenID Connect  via a separate module called the VC Verifier (essentially an OIDC server, requesting the suitable VCs and returning them to the calling application as OIDC claims). Additionally, the users are capable of reviewing the status of their application, withdrawing it and/or submitting a new one. Also, through specifically built interfaces the users are informed of any expired credentials and are capable of updating them (essentially updating the submitted information).

This repository contains a Java Spring Boot implementation of the aforementioned Social Solidarity Income Submission Web App.
In order to deploy this software please use the contained docker-compose.yml file. Alternatively, you can build the project via Maven and 
run the generated .jar file


The code contained within this repository was developed by the University of the Aegean during the
ultra-Social Benefits Transparency & Accountability (ultra-SocBenTΑ) project. This project was funded by Siemens via Settlement Agreement with Hellenic Republic 