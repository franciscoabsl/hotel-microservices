import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { AuthUserApi } from 'src/app/core/services/auth-user-api';

@Component({
  selector: 'app-login',
  imports: [FormsModule, CommonModule, ReactiveFormsModule],
  templateUrl: './login.html',
  styleUrl: './login.scss'
})
export class Login implements OnInit {
  form!: FormGroup;
  loading = false;
  errorMessage: string | null = null;
  returnUrl: string | null = null;

  constructor(
    private fb: FormBuilder,
    private authUserApi: AuthUserApi,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
      // inicializa o formulário reativo
      this.form = this.fb.group({
        email: ['', [Validators.required, Validators.email]],
        senha: ['', [Validators.required, Validators.minLength(6)]],
      });

      // captura returnUrl, se foi passado como query param
      const q = this.route.snapshot.queryParamMap.get('returnUrl');
      this.returnUrl = q ? q : null;
  }

  get email() {
    return this.form.get('email');
  }
  get senha() {
    return this.form.get('senha');
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.loading = true;
    this.errorMessage = null;

    const payload = {
      email: this.email!.value,
      senha: this.senha!.value,
    };

    this.authUserApi.login(payload).subscribe({
      next: () => {
        this.loading = false;
        // Login bem sucedido — navegar para returnUrl (se houver) ou para dashboard
        const target = this.returnUrl ?? '/home';
        this.router.navigateByUrl(target);
      },
      error: (err: unknown) => {
        this.loading = false;
        if (err instanceof Error) {
          this.errorMessage = err.message;
        } else {
          this.errorMessage = 'Erro ao autenticar. Tente novamente.';
        }
      },
    });
  }

  // opcional: método para login rápido durante dev
  devFillTestUser(): void {
    this.form.patchValue({
      email: 'aladim@gmail.com.xd',
      senha: '123456',
    });
  }
}
