import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { switchMap, tap, map } from 'rxjs/operators';
import { environment } from '../../../../environments/environment';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private base = `${environment.apiUrl}/learners/api`;

  constructor(private http: HttpClient) {}

  // Payer + télécharger facture en 1 clic
  payAndDownload(orderId: number): Observable<void> {
    return this.http.post(`${this.base}/orders/${orderId}/pay`, {}).pipe(
      switchMap(() =>
        this.http.get(`${this.base}/invoice`, {
          params: { orderId },
          responseType: 'blob'
        })
      ),
      tap((blob: Blob) => {
        const url = URL.createObjectURL(blob);
        const a   = document.createElement('a');
        a.href     = url;
        a.download = `facture-${orderId}.pdf`;
        a.click();
        URL.revokeObjectURL(url);
      }),
      map(() => void 0)
    );
  }
}