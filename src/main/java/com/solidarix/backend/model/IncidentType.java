package com.solidarix.backend.model;

public enum IncidentType {

    /*
    * Le demandeur d'aide ou le proposeur d'aide ne s'est pas présenté
    */
    NO_SHOW

    /*
    * L'un s'est mal comporté envers l'autre
    */
    , MISCONDUCT

    /*
    * Le proposeur d'aide n'a pas achevé sa mission en tant qu'aideur
    */
    , INCOMPLETE_HELP

    /*
    * Il y avait une erreur ou bien les 2 parties ne se sont pas très bien compris au moment de l'entraide et cela n'a pas abouti
    */
    , MISUNDERSTANDING

    /*
    * Toute autre situation non couverte
    */
    , OTHER

}
