import { HttpClient } from '@angular/common/http';
import {Component, OnInit} from '@angular/core';
import {AppService} from '@services/app.service';
import {DateTime} from 'luxon';

@Component({
    selector: 'app-user',
    templateUrl: './user.component.html',
    styleUrls: ['./user.component.scss']
})
export class UserComponent implements OnInit {
    email:string;
    id = Number(localStorage.getItem('id'));
    url ="http://localhost:8081/bnsp/api";
    constructor(private appService: AppService, private http: HttpClient) {}

    ngOnInit(): void {
    this.email = localStorage.getItem('email');

    }



    logout() {
        this.appService.logout();
    }

    formatDate(date) {
        return DateTime.fromISO(date).toFormat('dd LLL yyyy');
    }
}
